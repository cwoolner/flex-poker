package com.flexpoker.processmanagers;

import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.game.command.events.GameStartedEvent;
import com.flexpoker.table.command.commands.StartNewHandForNewGameCommand;
import com.flexpoker.table.command.commands.TableCommand;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.UUID;
import java.util.function.Consumer;

@Component
public class StartFirstHandProcessManager implements ProcessManager<GameStartedEvent> {

    private final CommandSender<TableCommand> tableCommandSender;

    @Inject
    public StartFirstHandProcessManager(CommandSender<TableCommand> tableCommandSender) {
        this.tableCommandSender = tableCommandSender;
    }

    @Async
    @Override
    public void handle(GameStartedEvent event) {
        var currentLevel = event.getBlindScheduleDTO().getCurrentLevel();
        var blindAmounts = event.getBlindScheduleDTO().getLevelToAmountsMap().get(currentLevel);

        Consumer<UUID> startFirstHandConsumer = tableId -> {
            var startNewHandForNewGameCommand = new StartNewHandForNewGameCommand(tableId, event.getAggregateId(),
                    blindAmounts.getSmallBlind(), blindAmounts.getBigBlind());
            tableCommandSender.send(startNewHandForNewGameCommand);
        };
        event.getTableIds().forEach(startFirstHandConsumer);
    }

}
