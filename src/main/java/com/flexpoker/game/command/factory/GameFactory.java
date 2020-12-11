package com.flexpoker.game.command.factory;

import java.util.List;

import com.flexpoker.game.command.aggregate.Game;
import com.flexpoker.game.command.commands.CreateGameCommand;
import com.flexpoker.game.command.events.GameEvent;

public interface GameFactory {

    Game createNew(CreateGameCommand command);

    Game createFrom(List<GameEvent> events);

}
