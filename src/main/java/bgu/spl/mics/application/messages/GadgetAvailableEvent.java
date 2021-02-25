package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;

public class GadgetAvailableEvent implements Event<Integer> {
    private String gadget;
    private Future<Integer> future = new Future<>();

    public GadgetAvailableEvent(String gadget){
        this.gadget=gadget;
    }

    public String getGadget() {
        return gadget;
    }

    public void setGadget(String gadget) {
        this.gadget = gadget;
    }

    public void setFuture(Future<Integer> future) {
        this.future = future;
    }

    public Future<Integer> getFuture() {
        return future;
    }
}