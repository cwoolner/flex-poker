package com.flexpoker.game.command.commands;

import java.util.UUID;

import com.flexpoker.framework.command.BaseCommand;
import com.flexpoker.game.command.framework.GameCommand;
import com.flexpoker.game.command.framework.GameCommandType;

public class IncrementBlindsCommand extends BaseCommand<GameCommandType> implements
        GameCommand {

    private static final GameCommandType TYPE = GameCommandType.IncrementBlinds;

    private final UUID aggregateId;

    public IncrementBlindsCommand(UUID aggregateId) {
        super(TYPE);
        this.aggregateId = aggregateId;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

}
