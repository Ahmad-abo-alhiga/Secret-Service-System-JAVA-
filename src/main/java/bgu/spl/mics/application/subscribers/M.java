package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Report;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
    private CountDownLatch latch;
    private int serialnumber;
    private Diary diary = Diary.getInstance();
    private int tick = 0;

    public M(int serialnumber, CountDownLatch latch) {
        super("M");
        this.latch = latch;
        this.serialnumber = serialnumber;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TerminateBroadcast.class, c -> terminate());
        subscribeBroadcast(TickBroadcast.class, c -> tick = c.getCurrtick().get());
        subscribeEvent(MissionReceivedEvent.class, c -> {
            diary.incrementTotal();
            Report r = new Report();
//------------------------agents event-------------------------------
            AgentsAvailableEvent a =
                    new AgentsAvailableEvent(c.getMission().getSerialAgentsNumbers(), c.getMission().getDuration());
            Future<List<String>> f = getSimplePublisher().sendEvent(a);
//------------------------Gadget event-------------------------------
            GadgetAvailableEvent g = new GadgetAvailableEvent(c.getMission().getGadget());
            Future<Integer> f1 = getSimplePublisher().sendEvent(g);
//------------------------the report --------------------------------
            if (f != null && f1 != null) {
                if (f.get() != null)
                    r.setAgentsNames(f.get());
                if (f1.get() != null && f1.isDone()) {
                    r.setQTime(f1.get());
                }
                if (f.get() != null) {
                    Future<Boolean> fut = getSimplePublisher().sendEvent(new SendEvent
                            (c.getMission().getSerialAgentsNumbers(), c.getMission().getDuration()));
                    long x = c.getMission().getDuration()*100;
                    try {
                        Thread.sleep(x);
                    }catch (InterruptedException e ){}
                }
                if (tick <= c.getMission().getTimeExpired()) {
                    while (r.getMoneypenny() == 0) {
                        r.setMoneypenny(a.getMoneynum());
                    }
                    r.setAgentsSerialNumbersNumber(a.getSerials());
                    r.setM(serialnumber);
                    r.setMissionName(c.getMission().getMissionName());
                    r.setTimeCreated(c.getTick());
                    r.setTimeIssued(c.getMission().getTimeIssued());
                    r.setGadgetName(c.getMission().getGadget());
                    if (!f.isDone() || (f.isDone() && f.get() == null)) {
                        String[] p = new String[1];
                        p[0] = r.getGadgetName();
                        getSimplePublisher().sendBroadcast(new GadgetReleaseEvent(p));
                    }
                    if (f1.isDone() && f.isDone() && f1.get() != null && f.get() != null) {
                        diary.addReport(r);
                        System.out.println(c.getMission().getMissionName() + " is reported");
                        complete(c, r);
                    } else {
                        complete(c, null);
                    }
                }
            }
        });
        latch.countDown();
    }
}
