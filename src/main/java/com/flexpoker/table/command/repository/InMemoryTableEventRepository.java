package com.flexpoker.table.command.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.table.command.framework.TableEvent;

@Profile("default")
@Repository
public class InMemoryTableEventRepository implements TableEventRepository {

    private final Map<UUID, List<TableEvent>> tableEventMap;

    public InMemoryTableEventRepository() {
        tableEventMap = new HashMap<>();
    }

    @Override
    public List<TableEvent> fetchAll(UUID id) {
        return tableEventMap.get(id);
    }

    @Override
    public List<TableEvent> setEventVersionsAndSave(int basedOnVersion, List<TableEvent> events) {
        var aggregateId = events.get(0).getAggregateId();

        if (!tableEventMap.containsKey(aggregateId)) {
            tableEventMap.put(aggregateId, new ArrayList<>());
        }

        var existingEvents = tableEventMap.get(aggregateId);
        if (existingEvents.size() != basedOnVersion) {
            throw new FlexPokerException("events to save are based on a different version of the aggregate");
        }

        for (int i = 0; i < events.size(); i++) {
            events.get(i).setVersion(basedOnVersion + i + 1);
        }

        tableEventMap.get(aggregateId).addAll(events);

        return events;
    }

}
