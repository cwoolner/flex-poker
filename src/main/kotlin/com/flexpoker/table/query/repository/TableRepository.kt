package com.flexpoker.table.query.repository

import com.flexpoker.table.query.dto.TableDTO
import java.util.UUID

interface TableRepository {
    fun fetchById(tableId: UUID): TableDTO
    fun save(tableDTO: TableDTO)
}