package com.flexpoker.table.query.repository.impl;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.dto.outgoing.TableDTO;

@Repository
public class InMemoryTableRepository implements TableRepository {

    private final Map<UUID, TableDTO> idToTableDTOMap;

    public InMemoryTableRepository() {
        idToTableDTOMap = new ConcurrentHashMap<>();
    }

    @Override
    public TableDTO fetchById(UUID tableId) {
        return idToTableDTOMap.get(tableId);
    }

    @Override
    public void save(TableDTO tableDTO) {
        idToTableDTOMap.put(tableDTO.getId(), tableDTO);
    }
}
