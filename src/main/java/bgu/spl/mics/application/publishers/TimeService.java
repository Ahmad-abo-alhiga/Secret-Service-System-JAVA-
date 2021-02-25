package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {
    private AtomicInteger time;
    private CountDownLatch latch;
    private AtomicInteger ticks;

    public TimeService(int duration, CountDownLatch latch) {
        super("TimeService");
        this.latch = latch;
        time = new AtomicInteger(duration);
        ticks = new AtomicInteger(1);
    }

    @Override
    protected void initialize() {
        latch.countDown();
    }

    @Override
    public void run() {
        initialize();
        while (ticks.get() <= time.get()) {
            System.out.println(ticks);
            TickBroadcast broadcast = new TickBroadcast(ticks , time.get());
            getSimplePublisher().sendBroadcast(broadcast);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ticks.incrementAndGet();
        }

        getSimplePublisher().sendBroadcast(new TerminateBroadcast());
    }
}

