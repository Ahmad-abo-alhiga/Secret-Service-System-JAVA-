package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Event;

public class GadgetReleaseEvent implements Broadcast {
    private String[] gadget;

    public GadgetReleaseEvent(String []gadget){
        this.gadget = gadget;
    }

    public String[] getGadget() {
        return gadget;
    }
}
