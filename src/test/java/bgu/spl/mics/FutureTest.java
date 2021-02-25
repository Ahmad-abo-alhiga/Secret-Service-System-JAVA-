package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {
    private Future<String> f1;
    @BeforeEach
    public void setUp(){
        f1 = new Future<String>();
    }

    @Test
    public void testGet(){
        String s = "ok";
        assertFalse(f1.isDone());
        f1.resolve(s);
        assertNotNull(f1.get());
        if (f1.isDone())
            assertNotNull(f1.get());
        f1.resolve(s);
        if (!f1.isDone())
            assertNull(f1.get());

    }
    @Test
    public void testResolve() {
        String str = "yes";
        String s = f1.get();
        assertNull(s);
        f1.resolve(str);
        assertNotNull(f1.get());
    }
    @Test
    public void testIsDone() {
        String s = "ok";
        if (f1.get() == null) {
            assertFalse(f1.isDone());
        }
        f1.resolve(s);

        if (f1.get() != null)
            assertTrue(f1.isDone());
    }
    @Test
    public void testGetLongTimeUnit() {
    }
}
