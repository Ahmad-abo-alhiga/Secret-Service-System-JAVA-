package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;

public class MissionReceivedEvent implements Event<Report> {
    private Future<Report> result;
    private MissionInfo mission;
    private int tick;
    public MissionReceivedEvent(MissionInfo mission , int tick) {
        this.result = new Future<Report>();
        this.mission = mission;
        this.tick = tick;
    }

    public int getTick() {
        return tick;
    }

    public void setMission(MissionInfo other) {
        this.mission = other;
    }


    public MissionInfo getMission() {
        return mission;
    }


}