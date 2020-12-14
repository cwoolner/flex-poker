package com.flexpoker.framework.event.subscriber

import com.flexpoker.framework.event.Event
import com.flexpoker.framework.event.EventHandler
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.util.Comparator
import java.util.HashMap
import java.util.UUID
import java.util.concurrent.PriorityBlockingQueue

@Scope("prototype")
@Component
class InMemoryThreadSafeEventSubscriberHelper<T : Event> {

    private val listOfEventsNeededToProcess: MutableMap<UUID, PriorityBlockingQueue<T>>
    private val nextExpectedEventVersion: MutableMap<UUID, Int>
    private var handlerMap: Map<Class<T>, EventHandler<T>>? = null

    fun receive(event: T) {
        synchronized(this) {
            listOfEventsNeededToProcess.putIfAbsent(event.aggregateId,
                PriorityBlockingQueue(10, Comparator.comparingInt { it.version }))
            nextExpectedEventVersion.putIfAbsent(event.aggregateId, 1)
            if (isExpectedEvent(event)) {
                handleEventAndRunAnyOthers(event)
            } else {
                listOfEventsNeededToProcess[event.aggregateId]!!.add(event)
            }
        }
    }

    private fun handleEventAndRunAnyOthers(event: T) {
        handleEvent(event)
        removeEventFromUnhandleList(event)
        incrementNextEventVersion(event)
        handleAnyPreviouslyUnhandledEvents(event)
    }

    private fun isExpectedEvent(event: Event): Boolean {
        val expectedEventVersion = nextExpectedEventVersion[event.aggregateId]!!.toInt()
        return expectedEventVersion == event.version
    }

    private fun handleEvent(event: T) {
        handlerMap!![event.javaClass]!!.handle(event)
    }

    private fun removeEventFromUnhandleList(event: Event) {
        listOfEventsNeededToProcess[event.aggregateId]!!.remove(event)
    }

    private fun incrementNextEventVersion(event: Event) {
        nextExpectedEventVersion.compute(event.aggregateId) { _: UUID, eventVersion: Int? -> eventVersion!!.inc() }
    }

    private fun handleAnyPreviouslyUnhandledEvents(event: T) {
        val earliestUnrunTableEvent = listOfEventsNeededToProcess[event.aggregateId]!!.peek()
        if (earliestUnrunTableEvent != null && isExpectedEvent(earliestUnrunTableEvent)) {
            handleEventAndRunAnyOthers(earliestUnrunTableEvent)
        }
    }

    fun setHandlerMap(handlerMap: Map<Class<T>, EventHandler<T>>?) {
        this.handlerMap = handlerMap
    }

    init {
        listOfEventsNeededToProcess = HashMap()
        nextExpectedEventVersion = HashMap()
    }
}