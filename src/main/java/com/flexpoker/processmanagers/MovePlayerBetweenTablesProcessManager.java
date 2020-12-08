package com.flexpoker.processmanagers;

import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.game.command.events.PlayerMovedToNewTableEvent;
import com.flexpoker.table.command.commands.AddPlayerCommand;
import com.flexpoker.table.command.commands.RemovePlayerCommand;
import com.flexpoker.table.command.commands.TableCommand;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class MovePlayerBetweenTablesProcessManager implements ProcessManager<PlayerMovedToNewTableEvent> {

    private final CommandSender<TableCommand> tableCommandSender;

    @Inject
    public MovePlayerBetweenTablesProcessManager(CommandSender<TableCommand> tableCommandSender) {
        this.tableCommandSender = tableCommandSender;
    }

    @Async
    @Override
    public void handle(PlayerMovedToNewTableEvent event) {
        var addPlayerTableCommand = new AddPlayerCommand(event.getToTableId(), event.getAggregateId(),
                event.getPlayerId(), event.getChips());
        tableCommandSender.send(addPlayerTableCommand);
        var removePlayerTableCommand = new RemovePlayerCommand(event.getFromTableId(), event.getAggregateId(),
                event.getPlayerId());
        tableCommandSender.send(removePlayerTableCommand);
    }

}
