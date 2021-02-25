package bgu.spl.mics.application;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {
        try {
            List<Thread> threadList = new LinkedList<>();
            CountDownLatch latch;
            JsonParser jason_parser = new JsonParser();
            Object obj = jason_parser.parse(new FileReader(args[0]));
            JsonObject json = (JsonObject) obj;
//----------------------inv load----------------------------
            JsonArray inv = json.getAsJsonArray("inventory");
            Inventory in = Inventory.getInstance();
            String[] tosend = new String[inv.size()];
            for (int i = 0; i < inv.size(); i++) {
                tosend[i] = inv.get(i).getAsString();
            }
            in.load(tosend);
//-----------------------Squad load---------------------------
            JsonArray squad = json.getAsJsonArray("squad");
            Squad sq = Squad.getInstance();
            Agent[] agentstosend = new Agent[squad.size()];
            for (int i = 0; i < squad.size(); i++) {
                Agent a = new Agent();
                JsonObject help = squad.get(i).getAsJsonObject();
                a.setName(help.get("name").getAsString());
                a.setSerialNumber(help.get("serialNumber").getAsString());
                agentstosend[i] = a;
            }
            sq.load(agentstosend);
//------------------------- Subsrcribers ---------------------------
            JsonObject services = json.getAsJsonObject("services");
            int M = services.get("M").getAsInt();
            int Moneypenny = services.get("Moneypenny").getAsInt();
            JsonArray intelle = services.get("intelligence").getAsJsonArray();
            int time = services.get("time").getAsInt();
            latch = new CountDownLatch(M + Moneypenny + intelle.size() + 2); // m + money + intele + time + q
//-------------------------- Q -------------------------------------
            Subscriber q = new Q(latch);
            Thread t2 = new Thread(q);
            t2.start();
            threadList.add(t2);
//------------------------- M --------------------------------------
            for (int i = 1; i <= M; i++) {
                Subscriber s = new M(i, latch);
                Thread t = new Thread(s);
                t.start();
                threadList.add(t);
            }
//------------------------- Money ------------------------------------
            for (int i = 1; i <= Moneypenny; i++) {
                Subscriber s = new Moneypenny(i, latch);
                Thread t = new Thread(s);
                t.start();
                threadList.add(t);
            }
//------------------------ Missions + Intelligence load--------------------------------------
            for (int i = 0; i < intelle.size(); i++) {
                ConcurrentHashMap<Integer, LinkedList<MissionInfo>> map = new ConcurrentHashMap<>();
                Intelligence s = new Intelligence(latch);
                JsonObject help = intelle.get(i).getAsJsonObject();
                JsonArray a = help.get("missions").getAsJsonArray();
                for (int j = 0; j < a.size(); j++) { //array of missions
                    MissionInfo toput = new MissionInfo();
                    JsonObject x = a.get(j).getAsJsonObject(); // the current mission
                    JsonArray z = x.get("serialAgentsNumbers").getAsJsonArray();
                    List<String> number = new LinkedList<>();
                    for (int l = 0; l < z.size(); l++) { // array of serial numbers
                        number.add(z.get(l).getAsString());
                    }
                    toput.setSerialAgentsNumbers(number);
                    toput.setDuration(x.get("duration").getAsInt());
                    toput.setGadget(x.get("gadget").getAsString());
                    toput.setMissionName(x.get("name").getAsString());
                    toput.setTimeExpired(x.get("timeExpired").getAsInt());
                    toput.setTimeIssued(x.get("timeIssued").getAsInt());

                    if(map.get(toput.getTimeIssued()) == null){
                        LinkedList<MissionInfo> add = new LinkedList<>();
                        add.add(toput);
                        map.put(toput.getTimeIssued() , add);
                    }else{
                        map.get(toput.getTimeIssued()).add(toput);
                    }
                }
                s.load(map);
                Thread t = new Thread(s);
                t.start();
                threadList.add(t);
            }
            //----------------------- time service -----------------------------
            TimeService timeService = new TimeService(time, latch);
            Thread t1 = new Thread(timeService);
            threadList.add(t1);
            t1.start();
            //----------------------------lets start----------------------------------------
            while (Thread.activeCount() > 2) {
            }
            Inventory i = Inventory.getInstance();
            Diary d = Diary.getInstance();
            d.printToFile(args[2]);
            i.printToFile(args[1]);
        } catch (FileNotFoundException ignored) {
        }
        System.out.println("we didnt sleep many days");
        System.out.println("                        " + "in order to make you , my lovely programs");
        System.out.println("we tried many tests");
        System.out.println("                   " + "whom were made with love and chocolates");
        System.out.println("all that in order to satisfy his");
        System.out.println("                                " + "his majesty our metargel");
        System.out.println("i give this small poem to our lovely SPL crew <3");

    }
}
