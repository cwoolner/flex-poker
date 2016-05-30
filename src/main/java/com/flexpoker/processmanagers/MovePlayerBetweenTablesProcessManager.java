package com.flexpoker.processmanagers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.game.command.events.PlayerMovedToNewTableEvent;
import com.flexpoker.table.command.commands.AddPlayerCommand;
import com.flexpoker.table.command.commands.RemovePlayerCommand;
import com.flexpoker.table.command.framework.TableCommandType;

@Component
public class MovePlayerBetweenTablesProcessManager
        implements ProcessManager<PlayerMovedToNewTableEvent> {

    private final CommandSender<TableCommandType> tableCommandSender;

    @Inject
    public MovePlayerBetweenTablesProcessManager(
            CommandSender<TableCommandType> tableCommandSender) {
        this.tableCommandSender = tableCommandSender;
    }

    @Async
    @Override
    public void handle(PlayerMovedToNewTableEvent event) {
        AddPlayerCommand addPlayerTableCommand = new AddPlayerCommand(
                event.getToTableId(), event.getAggregateId(),
                event.getPlayerId(), event.getChips());
        tableCommandSender.send(addPlayerTableCommand);
        RemovePlayerCommand removePlayerTableCommand = new RemovePlayerCommand(
                event.getFromTableId(), event.getAggregateId(),
                event.getPlayerId());
        tableCommandSender.send(removePlayerTableCommand);
    }
}
