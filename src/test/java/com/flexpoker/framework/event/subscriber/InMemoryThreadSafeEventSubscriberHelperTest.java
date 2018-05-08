package com.flexpoker.framework.event.subscriber;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.junit.Test;

import com.flexpoker.framework.event.Event;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.game.command.events.BlindsIncreasedEvent;
import com.flexpoker.game.command.events.GameFinishedEvent;
import com.flexpoker.game.command.events.GameJoinedEvent;

public class InMemoryThreadSafeEventSubscriberHelperTest {

    @Test
    public void testSentInOrderRunsInOrder() {
        var tableId = UUID.randomUUID();
        var event1 = new BlindsIncreasedEvent(tableId, 1);
        var event2 = new GameFinishedEvent(tableId, 2);
        var event3 = new GameJoinedEvent(tableId, 3, UUID.randomUUID());

        var handlerMap = new HashMap<Class<? extends Event>, EventHandler<? extends Event>>();

        final var eventRunList = new ArrayList<>();
        handlerMap.put(event1.getClass(), x -> { eventRunList.add(event1); });
        handlerMap.put(event2.getClass(), x -> { eventRunList.add(event2); });
        handlerMap.put(event3.getClass(), x -> { eventRunList.add(event3); });

        InMemoryThreadSafeEventSubscriberHelper sut = new InMemoryThreadSafeEventSubscriberHelper<>();
        sut.setHandlerMap(handlerMap);

        sut.receive(event1);
        sut.receive(event2);
        sut.receive(event3);

        assertEquals(event1, eventRunList.get(0));
        assertEquals(event2, eventRunList.get(1));
        assertEquals(event3, eventRunList.get(2));
    }

    @Test
    public void testSentInSwappedOrderRunsInOrder() {
        var tableId = UUID.randomUUID();
        var event1 = new BlindsIncreasedEvent(tableId, 1);
        var event2 = new GameFinishedEvent(tableId, 2);
        var event3 = new GameJoinedEvent(tableId, 3, UUID.randomUUID());

        var handlerMap = new HashMap<Class<? extends Event>, EventHandler<? extends Event>>();

        final var eventRunList = new ArrayList<>();
        handlerMap.put(event1.getClass(), x -> { eventRunList.add(event1); });
        handlerMap.put(event2.getClass(), x -> { eventRunList.add(event2); });
        handlerMap.put(event3.getClass(), x -> { eventRunList.add(event3); });

        InMemoryThreadSafeEventSubscriberHelper sut = new InMemoryThreadSafeEventSubscriberHelper<>();
        sut.setHandlerMap(handlerMap);

        sut.receive(event2);
        sut.receive(event3);
        sut.receive(event1);

        assertEquals(event1, eventRunList.get(0));
        assertEquals(event2, eventRunList.get(1));
        assertEquals(event3, eventRunList.get(2));
    }

}
