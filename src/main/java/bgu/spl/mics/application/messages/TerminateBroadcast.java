package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TerminateBroadcast implements Broadcast {
    private boolean terminate;

    public TerminateBroadcast(){
        terminate=false;
    }

    public void Terminate() {
        this.terminate = true;
    }
}
