package com.flexpoker.table.query.repository.impl

import com.flexpoker.config.ProfileNames
import com.flexpoker.table.query.dto.TableDTO
import com.flexpoker.table.query.repository.TableRepository
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import javax.inject.Inject

@Profile(ProfileNames.REDIS, ProfileNames.TABLE_QUERY_REDIS)
@Repository
class RedisTableRepository @Inject constructor(
    private val redisTemplateTableDTO: RedisTemplate<String, TableDTO>
) : TableRepository {

    private val idToLockMap: MutableMap<UUID, ReadWriteLock> = ConcurrentHashMap()

    companion object {
        private const val TABLE_DTO_NAMESPACE = "table-dto:"
    }

    override fun fetchById(tableId: UUID): TableDTO {
        try {
            idToLockMap[tableId]!!.readLock().lock()
            return redisTemplateTableDTO.opsForValue()[TABLE_DTO_NAMESPACE + tableId]!!
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
            val existingTableDTO = redisTemplateTableDTO.opsForValue()[TABLE_DTO_NAMESPACE + tableDTO.id]

            // only save if it's a new DTO or if the version is greater than the
            // current
            if (existingTableDTO == null || tableDTO.version > existingTableDTO.version) {
                redisTemplateTableDTO.opsForValue()[TABLE_DTO_NAMESPACE + tableDTO.id] = tableDTO
            }
        } finally {
            idToLockMap[tableDTO.id]!!.writeLock().unlock()
        }
    }

}