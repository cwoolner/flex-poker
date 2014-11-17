package com.flexpoker.table.command.commands;

import com.flexpoker.framework.command.BaseCommand;
import com.flexpoker.table.command.framework.TableCommand;
import com.flexpoker.table.command.framework.TableCommandType;

public class CreateTableCommand extends BaseCommand<TableCommandType> implements
        TableCommand {

    private static final TableCommandType TYPE = TableCommandType.CreateTable;

    public CreateTableCommand() {
        super(TYPE);
    }

}
