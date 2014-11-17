package com.flexpoker.table.command.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.flexpoker.table.command.framework.TableEvent;

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
    public void save(TableEvent event) {
        if (!tableEventMap.containsKey(event.getAggregateId())) {
            tableEventMap.put(event.getAggregateId(), new ArrayList<>());
        }
        tableEventMap.get(event.getAggregateId()).add(event);
    }

}
