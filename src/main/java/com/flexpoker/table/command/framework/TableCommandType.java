package com.flexpoker.table.command.framework;

import com.flexpoker.framework.command.CommandType;

public enum TableCommandType implements CommandType {

    CreateTable, StartNewHandForNewGame, ExpireActionOnTimer, StartNewHandForExistingTable, //

    Check, Call, Raise, Fold

}
