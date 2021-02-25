package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A Publisher only.
 * Holds a list of Info objects and sends them
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
    private AtomicInteger currtick = new AtomicInteger(1);
    private CountDownLatch latch;
    private ConcurrentHashMap<Integer, LinkedList<MissionInfo>> help = new ConcurrentHashMap<>();

    public Intelligence(CountDownLatch latch) {
        super("Intelligence");
        this.latch = latch;
    }


    public void load(ConcurrentHashMap<Integer, LinkedList<MissionInfo>> a) {
        help = new ConcurrentHashMap<>();
        help.putAll(a);
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TerminateBroadcast.class, c -> terminate());
        subscribeBroadcast(TickBroadcast.class, c -> {
            currtick = c.getCurrtick();
            for (Map.Entry<Integer, LinkedList<MissionInfo>> a : help.entrySet()) {
                if (a.getKey() <= currtick.get()) {
                    for (MissionInfo b : a.getValue()) {
                        help.remove(a.getKey());
                        if (b.getTimeExpired() >= currtick.get()) {
                            MissionReceivedEvent todo = new MissionReceivedEvent(b, currtick.get());
                            getSimplePublisher().sendEvent(todo);
                        }
                    }
                }
            }
        });
        latch.countDown();
    }
}