package com.flexpoker.core.api.actionon;

import java.util.Timer;

import com.flexpoker.model.Game;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;

public interface CreateAndStartActionOnTimerCommand {

    Timer execute(Game game, Table table, Seat seat);

}
