package bgu.spl.mics.example.messages;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Event;
import bgu.spl.mics.Future;

public class ExampleEvent implements Event<String>{

    private String senderName;

    public ExampleEvent(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getresult() {
        return null;
    }

    public void setresult(String other) {

    }

    public Future<String> getfuture() {
        return null;
    }

    public void setfuture(Future<String> a) {

    }

    public <T> Callback<T> getcallback() {
        return null;
    }
}