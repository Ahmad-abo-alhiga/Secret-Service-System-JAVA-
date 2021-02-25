package bgu.spl.mics.application.passiveObjects;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {
    private ConcurrentHashMap<String, Agent> agents = new ConcurrentHashMap<>();

    private static class SingletonSquad {
        private static Squad instance = new Squad();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static Squad getInstance() {
        return SingletonSquad.instance;
    }


    /**
     * Initializes the squad. This method adds all the agents to the squad.
     * <p>
     *
     * @param agents Data structure containing all data necessary for initialization
     *               of the squad.
     */
    public void load(Agent[] agents) {
        for (int i = 0; i < agents.length; i++) {
            this.agents.put(agents[i].getSerialNumber(), agents[i]);
        }
    }

    /**
     * Releases agents.
     */
    public void releaseAgents(List<String> serials) {  // what if two threads get in here ? , does it make an error.
        if (getAgents(serials)) {
            for (String a : serials) {
                agents.get(a).release();
            }
        }
    }

    /**
     * simulates executing a mission by calling sleep.
     *
     * @param time milliseconds to sleep
     */
    public void sendAgents(List<String> serials, int time) {           //how to synch only the ones that im using.
        time = time * 100; // each timetick is 100 milliseconds.
        ArrayList<String> serials1 = new ArrayList<>(serials);
        Collections.sort(serials1);
        try {
            Thread.sleep(time);
        } catch (Exception ignored) {
        }
        for (String a : serials1) {            // after the "time" has passed i marked them back to be released
            agents.get(a).release();
        }
    }

    /**
     * acquires an agent, i.e. holds the agent until the caller is done with it
     *
     * @param serials the serial numbers of the agents
     * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
     */
    public boolean getAgents(List<String> serials) {
        for (String a : serials) {
            if (!agents.containsKey(a)) {
                return false;
            }
        }
        for (String a : serials) {
            agents.get(a).acquire();
        }
        return true;
    }

    /**
     * gets the agents names
     *
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials) {
        List<String> names = new LinkedList<>();
        for (String a : serials) {
            names.add(agents.get(a).getName());
        }
        return names;
    }

}
