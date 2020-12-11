package com.flexpoker.table.command.repository;

import java.util.List;
import java.util.UUID;

import com.flexpoker.table.command.events.TableEvent;

public interface TableEventRepository {

    List<TableEvent> fetchAll(UUID id);

    List<TableEvent> setEventVersionsAndSave(int basedOnVersion, List<TableEvent> events);

}
