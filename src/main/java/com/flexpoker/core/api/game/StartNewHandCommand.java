package com.flexpoker.core.api.game;

import com.flexpoker.model.Game;
import com.flexpoker.model.Table;

public interface StartNewHandCommand {

    void execute(Game game, Table table);

}
