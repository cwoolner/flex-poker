package com.flexpoker.table.query.repository.impl

import com.flexpoker.config.ProfileNames
import com.flexpoker.table.query.dto.TableDTO
import com.flexpoker.table.query.repository.TableRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

@Profile(ProfileNames.DEFAULT, ProfileNames.TABLE_QUERY_INMEMORY)
@Repository
class InMemoryTableRepository : TableRepository {

    private val idToTableDTOMap: MutableMap<UUID, TableDTO> = ConcurrentHashMap()
    private val idToLockMap: MutableMap<UUID, ReadWriteLock> = ConcurrentHashMap()

    override fun fetchById(tableId: UUID): TableDTO {
        try {
            idToLockMap[tableId]!!.readLock().lock()
            return idToTableDTOMap[tableId]!!
        } finally {
            idToLockMap[tableId]!!.readLock().unlock()
        }
    }

    override fun save(tableDTO: TableDTO) {
        try {
            // used ConcurrentHashMap because this putIfAbsent works consistently without additional
            // locking.  regular HashMap was causing an incorrect lock to be unlocked in the finally clause
            // when run repeatedly in the unit test
            idToLockMap.putIfAbsent(tableDTO.id, ReentrantReadWriteLock())
            idToLockMap[tableDTO.id]!!.writeLock().lock()
            val existingTableDTO = idToTableDTOMap[tableDTO.id]

            // only save if it's a new DTO or if the version is greater than the
            // current
            if (existingTableDTO == null || tableDTO.version > existingTableDTO.version) {
                idToTableDTOMap[tableDTO.id] = tableDTO
            }
        } finally {
            idToLockMap[tableDTO.id]!!.writeLock().unlock()
        }
    }

}