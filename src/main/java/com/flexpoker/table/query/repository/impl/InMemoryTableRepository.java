package com.flexpoker.table.query.repository.impl;

import java.util.Map;
import java.util.NoSuchElementException;
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
        if (!idToTableDTOMap.containsKey(tableId)) {
            throw new NoSuchElementException();
        }

        return idToTableDTOMap.get(tableId);
    }

    @Override
    public void save(TableDTO tableDTO) {
        // get the tableId from the map if it exists so that it can be
        // synchronized on
        UUID tableId = idToTableDTOMap.keySet().stream() //
                .filter(x -> x.equals(tableDTO.getId())) //
                .findFirst() //
                .orElse(tableDTO.getId());
        synchronized (tableId) {
            TableDTO existingTableDTO = idToTableDTOMap.get(tableId);
            if (existingTableDTO == null) {
                idToTableDTOMap.put(tableDTO.getId(), tableDTO);
            } else {
                if (tableDTO.getVersion() > existingTableDTO.getVersion()) {
                    idToTableDTOMap.put(tableDTO.getId(), tableDTO);
                }
            }
        }
    }

}
