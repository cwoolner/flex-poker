package com.flexpoker.table.command.repository;

import java.util.List;
import java.util.UUID;

import com.flexpoker.table.command.framework.TableEvent;

public interface TableEventRepository {

    List<TableEvent> fetchAll(UUID id);

    void save(TableEvent event);

}
