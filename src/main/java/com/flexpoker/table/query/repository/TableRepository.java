package com.flexpoker.table.query.repository;

import java.util.UUID;

import com.flexpoker.web.model.table.TableViewModel;

public interface TableRepository {

    TableViewModel fetchById(UUID tableId);

    void save(TableViewModel tableViewModel);

}
