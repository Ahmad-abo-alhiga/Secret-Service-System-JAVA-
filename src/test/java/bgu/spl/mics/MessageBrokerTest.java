package bgu.spl.mics;

import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.subscribers.M;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MessageBrokerTest {
    MessageBroker f1;
    Event<List<String>> event;

    @BeforeEach
    public void setUp() {
        f1 = MessageBrokerImpl.getInstance();
        event = new AgentsAvailableEvent();

    }

    @Test
    public void testGetinstance() {
        MessageBroker x = MessageBrokerImpl.getInstance();
        assertSame(f1, x);
    }

    @Test
    public void testcomplete() {

    }

    @Test
    public void testSubsevent() {
    }
}
