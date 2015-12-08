package com.flexpoker.processmanagers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.game.command.commands.AttemptToStartNewHandCommand;
import com.flexpoker.game.command.framework.GameCommandType;
import com.flexpoker.table.command.events.HandCompletedEvent;

@Component
public class AttemptToStartNewHandForExistingTableProcessManager implements
        ProcessManager<HandCompletedEvent> {

    private final CommandSender<GameCommandType> gameCommandSender;

    @Inject
    public AttemptToStartNewHandForExistingTableProcessManager(
            CommandSender<GameCommandType> gameCommandSender) {
        this.gameCommandSender = gameCommandSender;
    }

    @Async
    @Override
    public void handle(HandCompletedEvent event) {
        AttemptToStartNewHandCommand command = new AttemptToStartNewHandCommand(
                event.getGameId(), event.getAggregateId());
        gameCommandSender.send(command);
    }
}
