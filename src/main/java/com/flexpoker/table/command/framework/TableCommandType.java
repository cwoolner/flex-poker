package com.flexpoker.table.command.framework;

import com.flexpoker.framework.command.CommandType;

public enum TableCommandType implements CommandType {

    CreateTable, StartNewHandForNewGame, Check, Call, Raise, Fold, ExpireActionOnTimer

}
