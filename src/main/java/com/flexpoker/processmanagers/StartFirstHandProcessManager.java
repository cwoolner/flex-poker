package com.flexpoker.processmanagers;

import java.util.UUID;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.CommandPublisher;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.game.command.events.GameStartedEvent;
import com.flexpoker.table.command.commands.StartNewHandForNewGameCommand;
import com.flexpoker.table.command.framework.TableCommandType;

@Component
public class StartFirstHandProcessManager implements ProcessManager<GameStartedEvent> {

    private final CommandPublisher<TableCommandType> tableCommandPublisher;

    @Inject
    public StartFirstHandProcessManager(
            CommandPublisher<TableCommandType> tableCommandPublisher) {
        this.tableCommandPublisher = tableCommandPublisher;
    }

    @Async
    @Override
    public void handle(GameStartedEvent event) {
        Consumer<UUID> startFirstHandConsumer = (UUID tableId) -> {
            StartNewHandForNewGameCommand startNewHandForNewGameCommand = new StartNewHandForNewGameCommand(
                    tableId, event.getAggregateId(),
                    event.getBlinds().getSmallBlind(),
                    event.getBlinds().getBigBlind());
            tableCommandPublisher.publish(startNewHandForNewGameCommand);
        };
        event.getTableIds().forEach(startFirstHandConsumer);
    }
}
