package com.flexpoker.framework.domain;

import java.util.ArrayList;
import java.util.List;

import com.flexpoker.framework.event.Event;

public abstract class AggregateRoot<T extends Event> {

    private final List<T> newEvents = new ArrayList<>();

    private final List<T> appliedEvents = new ArrayList<>();

    protected void addNewEvent(T event) {
        newEvents.add(event);
    }

    protected void addAppliedEvent(T event) {
        appliedEvents.add(event);
    }

    public List<T> fetchNewEvents() {
        return new ArrayList<>(newEvents);
    }

    public List<T> fetchAppliedEvents() {
        return new ArrayList<>(appliedEvents);
    }

    public abstract void applyAllHistoricalEvents(List<T> events);

}
