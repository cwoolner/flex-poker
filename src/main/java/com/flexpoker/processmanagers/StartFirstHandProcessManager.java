package com.flexpoker.processmanagers;

import java.util.UUID;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.game.command.aggregate.BlindAmounts;
import com.flexpoker.game.command.events.GameStartedEvent;
import com.flexpoker.table.command.commands.StartNewHandForNewGameCommand;
import com.flexpoker.table.command.framework.TableCommandType;

@Component
public class StartFirstHandProcessManager implements ProcessManager<GameStartedEvent> {

    private final CommandSender<TableCommandType> tableCommandSender;

    @Inject
    public StartFirstHandProcessManager(
            CommandSender<TableCommandType> tableCommandSender) {
        this.tableCommandSender = tableCommandSender;
    }

    @Async
    @Override
    public void handle(GameStartedEvent event) {
        BlindAmounts blindAmounts = event.getBlindSchedule().getCurrentBlindAmounts();

        Consumer<UUID> startFirstHandConsumer = (UUID tableId) -> {
            StartNewHandForNewGameCommand startNewHandForNewGameCommand = new StartNewHandForNewGameCommand(
                    tableId, event.getAggregateId(),
                    blindAmounts.getSmallBlind(), blindAmounts.getBigBlind());
            tableCommandSender.send(startNewHandForNewGameCommand);
        };
        event.getTableIds().forEach(startFirstHandConsumer);
    }
}
