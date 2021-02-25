package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class SendEvent implements Event<Boolean> {
    private List<String> serials;
    private int time;

    public SendEvent(List<String> serials, int Time) {
        this.serials = serials;
        this.time = Time;
    }


    public int getTime() {
        return time;
    }

    public List<String> getSerials() {
        return serials;
    }
}
