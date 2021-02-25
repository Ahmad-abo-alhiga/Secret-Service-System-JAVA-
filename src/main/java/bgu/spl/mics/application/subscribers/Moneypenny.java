package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.SendEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.concurrent.CountDownLatch;


/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
    private Squad sq = Squad.getInstance();
    private int serialnumber;
    private CountDownLatch latch;

    public Moneypenny(int serialnumber, CountDownLatch latch) {
        super("Moneypenny");
        this.serialnumber = serialnumber;
        this.latch = latch;
    }

    @Override
    protected void initialize() {
        this.subscribeEvent(SendEvent.class, c -> {
            sq.sendAgents(c.getSerials(), c.getTime());
            complete(c, true);
        });
        this.subscribeBroadcast(TerminateBroadcast.class, c -> terminate());
        this.subscribeEvent(AgentsAvailableEvent.class, c -> {
            c.setMoneynum(serialnumber);
            if (sq.getAgents(c.getSerials())) {
                complete(c, sq.getAgentsNames(c.getSerials()));
            } else complete(c, null);
        });
        latch.countDown();
    }
}
