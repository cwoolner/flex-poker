package com.flexpoker.table.query.repository;

import java.util.UUID;

import com.flexpoker.web.dto.outgoing.TableDTO;

public interface TableRepository {

    TableDTO fetchById(UUID tableId);

    void save(TableDTO tableDTO);

}
