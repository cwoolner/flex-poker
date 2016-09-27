package com.flexpoker.web.controller;

import java.security.Principal;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.login.repository.LoginRepository;
import com.flexpoker.table.command.commands.CallCommand;
import com.flexpoker.table.command.commands.CheckCommand;
import com.flexpoker.table.command.commands.FoldCommand;
import com.flexpoker.table.command.commands.RaiseCommand;
import com.flexpoker.table.command.framework.TableCommandType;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.dto.incoming.CallTableActionDTO;
import com.flexpoker.web.dto.incoming.CheckTableActionDTO;
import com.flexpoker.web.dto.incoming.FoldTableActionDTO;
import com.flexpoker.web.dto.incoming.RaiseTableActionDTO;
import com.flexpoker.web.dto.outgoing.TableDTO;

@Controller
public class TableController {

    private final CommandSender<TableCommandType> commandSender;

    private final LoginRepository loginRepository;

    private final TableRepository tableRepository;

    @Inject
    public TableController(CommandSender<TableCommandType> commandSender,
            LoginRepository loginRepository, TableRepository tableRepository) {
        this.commandSender = commandSender;
        this.loginRepository = loginRepository;
        this.tableRepository = tableRepository;
    }

    @SubscribeMapping("/topic/game/{gameId}/table/{tableId}")
    public TableDTO fetchTable(@DestinationVariable UUID gameId,
            @DestinationVariable UUID tableId) {
        return tableRepository.fetchById(tableId);
    }

    @MessageMapping("/app/check")
    public void check(CheckTableActionDTO model, Principal principal) {
        UUID playerId = loginRepository.fetchAggregateIdByUsername(principal.getName());
        commandSender.send(new CheckCommand(model.getTableId(), model.getGameId(),
                playerId));
    }

    @MessageMapping("/app/fold")
    public void fold(FoldTableActionDTO model, Principal principal) {
        UUID playerId = loginRepository.fetchAggregateIdByUsername(principal.getName());
        commandSender.send(new FoldCommand(model.getTableId(), model.getGameId(),
                playerId));
    }

    @MessageMapping("/app/call")
    public void call(CallTableActionDTO model, Principal principal) {
        UUID playerId = loginRepository.fetchAggregateIdByUsername(principal.getName());
        commandSender.send(new CallCommand(model.getTableId(), model.getTableId(),
                playerId));
    }

    @MessageMapping("/app/raise")
    public void raise(RaiseTableActionDTO model, Principal principal) {
        UUID playerId = loginRepository.fetchAggregateIdByUsername(principal.getName());
        commandSender.send(new RaiseCommand(model.getTableId(), model.getGameId(),
                playerId, model.getRaiseToAmount()));
    }

}
