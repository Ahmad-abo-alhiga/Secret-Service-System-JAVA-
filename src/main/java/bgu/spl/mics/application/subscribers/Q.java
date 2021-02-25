package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetReleaseEvent;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {
	private Inventory inv = Inventory.getInstance();
	private AtomicInteger tick = new AtomicInteger(1);
	private CountDownLatch latch;

	public Q(CountDownLatch latch) {
		super("Q");
		this.latch = latch;
	}

	@Override
	protected void initialize() {
		latch.countDown();
		subscribeBroadcast(GadgetReleaseEvent.class, c -> {
			inv.load(c.getGadget());
			System.out.println("Gadget released  "+ Arrays.toString(c.getGadget()));
		});
		this.subscribeBroadcast(TerminateBroadcast.class, c -> terminate());
		this.subscribeBroadcast(TickBroadcast.class, c -> this.tick = new AtomicInteger(c.getCurrtick().get()));
		this.subscribeEvent(GadgetAvailableEvent.class, c -> {
			if(inv.getItem(c.getGadget())){
				complete(c , tick.get());
			} else complete(c , null);
		});
	}
}