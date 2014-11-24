package com.flexpoker.table.query.repository.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.model.table.TableViewModel;

@Repository
public class InMemoryTableRepository implements TableRepository {

    private final Map<UUID, TableViewModel> idToTableDTOMap;

    public InMemoryTableRepository() {
        idToTableDTOMap = new HashMap<>();
    }

    @Override
    public TableViewModel fetchById(UUID tableId) {
        return idToTableDTOMap.get(tableId);
    }

    @Override
    public void save(TableViewModel tableViewModel) {
        idToTableDTOMap.put(tableViewModel.getId(), tableViewModel);
    }
}
