package com.flexpoker.framework.event.subscriber

import com.flexpoker.framework.event.Event
import com.flexpoker.framework.event.EventHandler
import com.flexpoker.game.command.events.BlindsIncreasedEvent
import com.flexpoker.game.command.events.GameFinishedEvent
import com.flexpoker.game.command.events.GameJoinedEvent
import com.flexpoker.test.util.UnitTestClass
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.UUID

@UnitTestClass
class InMemoryThreadSafeEventSubscriberHelperTest {

    @Test
    fun testSentInOrderRunsInOrder() {
        val tableId = UUID.randomUUID()
        val event1 = BlindsIncreasedEvent(tableId)
        event1.version = 1
        val event2 = GameFinishedEvent(tableId)
        event2.version = 2
        val event3 = GameJoinedEvent(tableId, UUID.randomUUID())
        event3.version = 3
        val handlerMap = HashMap<Class<out Event>, EventHandler<out Event>>()
        val eventRunList = ArrayList<Any>()
        handlerMap[event1.javaClass] = EventHandler { eventRunList.add(event1) }
        handlerMap[event2.javaClass] = EventHandler { eventRunList.add(event2) }
        handlerMap[event3.javaClass] = EventHandler { eventRunList.add(event3) }
        val sut: InMemoryThreadSafeEventSubscriberHelper<Event> = InMemoryThreadSafeEventSubscriberHelper()
        sut.setHandlerMap(handlerMap as Map<Class<Event>, EventHandler<Event>>)
        sut.receive(event1)
        sut.receive(event2)
        sut.receive(event3)
        assertEquals(event1, eventRunList[0])
        assertEquals(event2, eventRunList[1])
        assertEquals(event3, eventRunList[2])
    }

    @Test
    fun testSentInSwappedOrderRunsInOrder() {
        val tableId = UUID.randomUUID()
        val event1 = BlindsIncreasedEvent(tableId)
        event1.version = 1
        val event2 = GameFinishedEvent(tableId)
        event2.version = 2
        val event3 = GameJoinedEvent(tableId, UUID.randomUUID())
        event3.version = 3
        val handlerMap = HashMap<Class<out Event>, EventHandler<out Event>>()
        val eventRunList = ArrayList<Any>()
        handlerMap[event1.javaClass] = EventHandler { eventRunList.add(event1) }
        handlerMap[event2.javaClass] = EventHandler { eventRunList.add(event2) }
        handlerMap[event3.javaClass] = EventHandler { eventRunList.add(event3) }
        val sut: InMemoryThreadSafeEventSubscriberHelper<Event> = InMemoryThreadSafeEventSubscriberHelper()
        sut.setHandlerMap(handlerMap as Map<Class<Event>, EventHandler<Event>>)
        sut.receive(event2)
        sut.receive(event3)
        sut.receive(event1)
        assertEquals(event1, eventRunList[0])
        assertEquals(event2, eventRunList[1])
        assertEquals(event3, eventRunList[2])
    }
}