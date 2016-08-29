package com.flexpoker.framework.event.subscriber;

import java.util.Comparator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.Event;
import com.flexpoker.framework.event.EventHandler;

@Scope("prototype")
@Component
public class InMemoryThreadSafeEventSubscriberHelper<T extends Event> {

    private final Map<UUID, PriorityBlockingQueue<T>> listOfEventsNeededToProcess;

    private final Map<UUID, Integer> nextExpectedEventVersion;

    private Map<Class<T>, EventHandler<T>> handlerMap;
    
    public InMemoryThreadSafeEventSubscriberHelper() {
        listOfEventsNeededToProcess = new ConcurrentHashMap<>();
        nextExpectedEventVersion = new ConcurrentHashMap<>();
    }

    public void receive(T event) {
        synchronized (this) {
            listOfEventsNeededToProcess.putIfAbsent(event.getAggregateId(), new PriorityBlockingQueue<>(10, Comparator.comparingInt(Event::getVersion)));
            nextExpectedEventVersion.putIfAbsent(event.getAggregateId(), Integer.valueOf(1));

            if (isExpectedEvent(event)) {
                handleEventAndRunAnyOthers(event);
            } else {
                listOfEventsNeededToProcess.get(event.getAggregateId()).add(event);
            }
        }
    }

    private void handleEventAndRunAnyOthers(T event) {
        handleEvent(event);
        removeEventFromUnhandleList(event);
        incrementNextEventVersion(event);
        handleAnyPreviouslyUnhandledEvents(event);
    }

    private boolean isExpectedEvent(Event event) {
        int expectedEventVersion = nextExpectedEventVersion.get(event.getAggregateId())
                .intValue();
        return expectedEventVersion == event.getVersion();
    }

    private void handleEvent(T event) {
        handlerMap.get(event.getClass()).handle(event);
    }

    private void removeEventFromUnhandleList(Event event) {
        listOfEventsNeededToProcess.get(event.getAggregateId()).remove(event);
    }

    private void incrementNextEventVersion(Event event) {
        nextExpectedEventVersion.compute(event.getAggregateId(),
                (eventId, eventVersion) -> eventVersion + 1);
    }

    private void handleAnyPreviouslyUnhandledEvents(T event) {
        T earliestUnrunTableEvent = listOfEventsNeededToProcess.get(event.getAggregateId()).peek();
        if (earliestUnrunTableEvent != null && isExpectedEvent(earliestUnrunTableEvent)) {
            handleEventAndRunAnyOthers(earliestUnrunTableEvent);
        }
    }

    public void setHandlerMap(Map<Class<T>, EventHandler<T>> handlerMap) {
        this.handlerMap = handlerMap;
    }

}
