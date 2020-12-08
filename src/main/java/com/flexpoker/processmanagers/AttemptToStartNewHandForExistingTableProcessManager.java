package com.flexpoker.processmanagers;

import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.game.command.commands.AttemptToStartNewHandCommand;
import com.flexpoker.game.command.commands.GameCommand;
import com.flexpoker.table.command.events.HandCompletedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class AttemptToStartNewHandForExistingTableProcessManager implements ProcessManager<HandCompletedEvent> {

    private final CommandSender<GameCommand> gameCommandSender;

    @Inject
    public AttemptToStartNewHandForExistingTableProcessManager(CommandSender<GameCommand> gameCommandSender) {
        this.gameCommandSender = gameCommandSender;
    }

    @Async
    @Override
    public void handle(HandCompletedEvent event) {
        var command = new AttemptToStartNewHandCommand(event.getGameId(), event.getAggregateId(),
                event.getPlayerToChipsAtTableMap());
        gameCommandSender.send(command);
    }

}
