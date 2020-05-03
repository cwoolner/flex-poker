package com.flexpoker.table.query.repository.impl;

import java.util.UUID;

import javax.inject.Inject;

import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.flexpoker.config.ProfileNames;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.dto.outgoing.TableDTO;

@Profile({ ProfileNames.REDIS, ProfileNames.TABLE_QUERY_REDIS })
@Repository
public class RedisTableRepository implements TableRepository {

    private static final String TABLE_DTO_NAMESPACE = "table-dto:";

    private final RedisTemplate<String, TableDTO> redisTemplateTableDTO;

    @Inject
    public RedisTableRepository(RedisTemplate<String, TableDTO> redisTemplateTableDTO) {
        this.redisTemplateTableDTO = redisTemplateTableDTO;
    }

    @Override
    public TableDTO fetchById(UUID tableId) {
        return redisTemplateTableDTO.opsForValue().get(TABLE_DTO_NAMESPACE + tableId);
    }

    @Override
    public void save(TableDTO tableDTO) {
        redisTemplateTableDTO.opsForValue().set(TABLE_DTO_NAMESPACE + tableDTO.getId(), tableDTO);
    }

}
