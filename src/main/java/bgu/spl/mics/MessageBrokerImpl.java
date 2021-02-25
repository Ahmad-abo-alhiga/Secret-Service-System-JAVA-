package bgu.spl.mics;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
    private static class Singletonmessegebroker {
        private static MessageBroker instance = new MessageBrokerImpl();
    }

    private ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<Subscriber>> map1 = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Subscriber, LinkedBlockingQueue<Message>> SubscribersMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Event, Future> FutureMap = new ConcurrentHashMap<>();

    /**
     * Retrieves the single instance of this class.
     */
    synchronized public static MessageBroker getInstance() {
        return Singletonmessegebroker.instance;
    }

    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
        map1.putIfAbsent(type, new ConcurrentLinkedQueue<>());
        map1.get(type).add(m);
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
        map1.putIfAbsent(type, new ConcurrentLinkedQueue<>());
        map1.get(type).add(m);
    }

    @Override
    public <T> void complete(Event<T> e, T result) {
        FutureMap.get(e).resolve(result);
    } // get it from the map first

    @Override
    public void sendBroadcast(Broadcast b) {
        ConcurrentLinkedQueue<Subscriber> q = map1.get(b.getClass());
        if (q == null)
            return;
        q.forEach(subscriber -> {
            LinkedBlockingQueue<Message> q2 = SubscribersMap.get(subscriber);
            if (q2 != null)
                q2.add(b);
        });
    }

    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        Future<T> f = new Future<>();
        FutureMap.put(e, f);
        ConcurrentLinkedQueue<Subscriber> q = map1.get(e.getClass()); // the subscribers subscribed to e
        Subscriber s;
        if (q == null)
            return null;

        synchronized (e.getClass()) { //round robin
            if (q.isEmpty()) {
                return null;
            }
            s = q.poll();
            q.add(s);
        }

        synchronized (s) {
            LinkedBlockingQueue<Message> q2 = SubscribersMap.get(s); // adding e to the matching subscriber
            if (q2 == null)
                return null;
            q2.add(e);
        }
        return f;
    }

    @Override
    public void register(Subscriber m) {
        SubscribersMap.put(m, new LinkedBlockingQueue<>());
    }

    @Override
    public void unregister(Subscriber s) {
        map1.forEach((key, value) -> {
            synchronized (key) {
                value.remove(s);
            }
        });
        LinkedBlockingQueue<Message> q;
        synchronized (s) {
            q = SubscribersMap.remove(s);
        }
        while (!q.isEmpty()) {
            Message m = q.poll();
            Future<?> future = FutureMap.get(m);
            if (future != null)
                future.resolve(null);
        }
    }

    @Override
    public Message awaitMessage(Subscriber m) throws InterruptedException {
        return SubscribersMap.get(m).take();
    }
}
