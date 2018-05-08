package com.flexpoker.table.query.repository.impl;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.stereotype.Repository;

import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.dto.outgoing.TableDTO;

@Repository
public class InMemoryTableRepository implements TableRepository {

    private final Map<UUID, TableDTO> idToTableDTOMap;

    private final Map<UUID, ReadWriteLock> idToLockMap;

    public InMemoryTableRepository() {
        idToTableDTOMap = new ConcurrentHashMap<>();
        idToLockMap = new ConcurrentHashMap<>();
    }

    @Override
    public TableDTO fetchById(UUID tableId) {
        try {
            idToLockMap.get(tableId).readLock().lock();
            return idToTableDTOMap.get(tableId);
        } finally {
            idToLockMap.get(tableId).readLock().unlock();
        }
    }

    @Override
    public void save(TableDTO tableDTO) {
        try {
            idToLockMap.putIfAbsent(tableDTO.getId(),
                    new ReentrantReadWriteLock());
            idToLockMap.get(tableDTO.getId()).writeLock().lock();

            var existingTableDTO = idToTableDTOMap.get(tableDTO.getId());

            // only save if it's a new DTO or if the version is greater than the
            // current
            if (existingTableDTO == null || tableDTO.getVersion() > existingTableDTO.getVersion()) {
                idToTableDTOMap.put(tableDTO.getId(), tableDTO);
            }
        } finally {
            idToLockMap.get(tableDTO.getId()).writeLock().unlock();
        }
    }

}
