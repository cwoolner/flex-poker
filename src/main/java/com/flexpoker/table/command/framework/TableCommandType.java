package com.flexpoker.table.command.framework;

import com.flexpoker.framework.command.CommandType;

public enum TableCommandType implements CommandType {

    CreateTable, StartNewHandForNewGame, //

    ExpireActionOnTimer, TickActionOnTimer, StartNewHandForExistingTable, //

    Pause, Resume, AddPlayer, RemovePlayer, //

    Check, Call, Raise, Fold

}
