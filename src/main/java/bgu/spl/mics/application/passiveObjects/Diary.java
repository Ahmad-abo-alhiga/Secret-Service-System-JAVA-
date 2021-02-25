package bgu.spl.mics.application.passiveObjects;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing the diary where all reports are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Diary {
    private List<Report> reports = new LinkedList<>();
    private AtomicInteger total = new AtomicInteger(0);

    private static class SingletonDiary {
        private static Diary instane = new Diary();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static Diary getInstance() {
        return SingletonDiary.instane;
    }

    public void incrementTotal() {
        total.incrementAndGet();
    }

    /**
     * adds a report to the diary
     *
     * @param reportToAdd - the report to add
     */
    synchronized public void addReport(Report reportToAdd) {
        reports.add(reportToAdd);
    }

    /**
     * <p>
     * Prints to a file name @filename a serialized object List<Report> which is a
     * List of all the reports in the diary.
     * This method is called by the main method in order to generate the output.
     */
    public void printToFile(String filename) {
        List<Report> toprint = new LinkedList<>(reports);
        System.out.println("Reports:" + toprint.size() + " total:" + total);
        for (Report a : toprint) {
            System.out.println("name:" + a.getMissionName() + " M:" + a.getM() + " gadget:" + a.getGadgetName()
                    + " moneypenny:" + a.getMoneypenny() + " Qtime:" + a.getQTime() + " creationtime:" + a.getTimeCreated()
                    + " timeissued:" + a.getTimeIssued());
        }
        JSONArray reports = new JSONArray();
        for (Report a : toprint) {
            JSONObject r = new JSONObject();
            r.put("mission name", a.getMissionName());
            r.put("m", a.getM());
            r.put("moneypenny", a.getMoneypenny());
            JSONArray nums = new JSONArray();
            for (String x : a.getAgentsSerialNumbersNumber()) {
                nums.add(x);
            }
            r.put("agentsSerialNumbers", nums);
            JSONArray names = new JSONArray();
            for (String x : a.getAgentsNames()) {
                names.add(x);
            }
            r.put("agentsNames", names);
            reports.add(r);
        }
        JSONObject time = new JSONObject();
        time.put("total", this.total.intValue());
        try {
            FileWriter file = new FileWriter(filename);
            file.write(reports.toJSONString());
            file.write(time.toJSONString());
            file.flush();
            file.close();
        } catch (Exception e) {
        }


    }
}
