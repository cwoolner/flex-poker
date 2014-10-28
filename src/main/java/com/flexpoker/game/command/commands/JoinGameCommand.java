package com.flexpoker.game.command.commands;

import java.util.UUID;

import com.flexpoker.framework.command.BaseCommand;
import com.flexpoker.game.command.framework.GameCommand;
import com.flexpoker.game.command.framework.GameCommandType;

public class JoinGameCommand extends BaseCommand<GameCommandType> implements GameCommand {

    private static final GameCommandType TYPE = GameCommandType.JoinGame;

    private final UUID aggregateId;

    private final UUID playerId;

    public JoinGameCommand(UUID aggregateId, UUID playerId) {
        super(TYPE);
        this.aggregateId = aggregateId;
        this.playerId = playerId;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

}
