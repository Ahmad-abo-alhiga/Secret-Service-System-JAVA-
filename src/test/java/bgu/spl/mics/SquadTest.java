package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SquadTest {
    Squad f1;
    Agent a = new Agent();
    Agent b = new Agent();
    Agent c = new Agent();
    Agent[] agents = {a, b, c};
    Map<String, Agent> help;

    @BeforeEach
    public void setUp() {
        f1 = Squad.getInstance();
    }

    @Test
    public void testgetInstance() {
        Squad sq = Squad.getInstance();
        assertTrue(f1 == sq);
    }

    @Test
    public void testLoad() {
        f1.load(agents);
    }

    @Test
    public void testrealeseagent() {
        help.put("a", a);
        help.put("b", b);
        help.put("c", c);
        List<String> a = new LinkedList<>();
        a.add("a");
        a.add("b");
        a.add("c");
        f1.releaseAgents(a);
    }
}