package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;

import java.util.LinkedList;
import java.util.List;

public class AgentsAvailableEvent implements Event<List<String>> {
    private List<String> serials;
    private int moneynum;
    private List<String> names = new LinkedList<>();
    private Future<List<String>> future = new Future<>();
    private int duration;

    public AgentsAvailableEvent() {
    }

    public int getDuration() {
        return duration;
    }

    synchronized public void setMoneynum(int moneynum) {
        this.moneynum = moneynum;
    }

    public int getMoneynum() {
        return moneynum;
    }


    public AgentsAvailableEvent(List<String> serials, int duration) {
        this.serials = serials;
        this.duration = duration;
    }

    public void setSerials(List<String> serials) {
        this.serials = serials;
    }

    public List<String> getSerials() {
        return serials;
    }

    public List<String> getNames() {
        return names;
    }

    public Future<List<String>> getFuture() {
        return future;
    }

    public void setFuture(Future<List<String>> future) {
        this.future = future;
    }

}