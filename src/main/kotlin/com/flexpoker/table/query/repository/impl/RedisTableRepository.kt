package com.flexpoker.table.query.repository.impl

import com.flexpoker.config.ProfileNames
import com.flexpoker.table.query.dto.TableDTO
import com.flexpoker.table.query.repository.TableRepository
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.stereotype.Repository
import java.util.UUID
import javax.inject.Inject


@Profile(ProfileNames.REDIS, ProfileNames.TABLE_QUERY_REDIS)
@Repository
class RedisTableRepository @Inject constructor(
    private val redisTemplateTableDTO: RedisTemplate<String, TableDTO>,
    private val checkAndSetScript: RedisScript<Boolean>,
) : TableRepository {

    companion object {
        private const val TABLE_DTO_NAMESPACE = "table-dto"
    }

    private fun redisKey(tableId: UUID) = "$TABLE_DTO_NAMESPACE:$tableId"

    override fun fetchById(tableId: UUID): TableDTO {
        return redisTemplateTableDTO.opsForValue()[redisKey(tableId)]!!
    }

    override fun save(tableDTO: TableDTO) {
        redisTemplateTableDTO.execute(
            checkAndSetScript,
            listOf(redisKey(tableDTO.id)),
            tableDTO,
        )
    }

}