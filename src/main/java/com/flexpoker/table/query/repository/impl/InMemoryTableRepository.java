package com.flexpoker.table.query.repository.impl;

import com.flexpoker.config.ProfileNames;
import com.flexpoker.table.query.dto.TableDTO;
import com.flexpoker.table.query.repository.TableRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Profile({ ProfileNames.DEFAULT, ProfileNames.TABLE_QUERY_INMEMORY })
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
            // used ConcurrentHashMap because this putIfAbsent works consistently without additional
            // locking.  regular HashMap was causing an incorrect lock to be unlocked in the finally clause
            // when run repeatedly in the unit test
            idToLockMap.putIfAbsent(tableDTO.getId(), new ReentrantReadWriteLock());
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
