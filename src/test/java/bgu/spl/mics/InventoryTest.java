package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.subscribers.Q;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    Inventory f1;
    String[] a = {"x", "y", "z"};

    @BeforeEach
    public void setUp() {
        f1 = Inventory.getInstance();
    }

    @Test
    public void testgetInstance() {
        Inventory inv = Inventory.getInstance();
        assertTrue(f1 == inv );
    }

    @Test
    public void testLoadAndgetItem() {
        f1.load(a);
        assertTrue(f1.getItem("x"));
        assertFalse(f1.getItem("d"));
        assertFalse(f1.getItem("u"));
        assertFalse(f1.getItem("i"));
        assertTrue(f1.getItem("y"));
        assertTrue(f1.getItem("z"));
    }

    @Test
    public void testPrint() {
        String filename ="";
        f1.printToFile(filename);
        assertTrue(a[0] == "x");
        assertTrue(a[1] == "y");
        assertTrue(a[2] == "z");
    }
}