package com.flexpoker.table.query.repository;

import com.flexpoker.table.query.dto.TableDTO;

import java.util.UUID;

public interface TableRepository {

    TableDTO fetchById(UUID tableId);

    void save(TableDTO tableDTO);

}
