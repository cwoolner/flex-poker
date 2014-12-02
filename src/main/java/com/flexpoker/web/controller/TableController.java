package com.flexpoker.web.controller;

import java.security.Principal;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.flexpoker.framework.command.CommandPublisher;
import com.flexpoker.login.query.repository.LoginRepository;
import com.flexpoker.table.command.commands.CallCommand;
import com.flexpoker.table.command.commands.CheckCommand;
import com.flexpoker.table.command.commands.FoldCommand;
import com.flexpoker.table.command.commands.RaiseCommand;
import com.flexpoker.table.command.framework.TableCommandType;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.model.table.TableViewModel;
import com.flexpoker.web.model.table.handaction.BaseHandActionViewModel;
import com.flexpoker.web.model.table.handaction.RaiseHandActionViewModel;

@Controller
public class TableController {

    private final CommandPublisher<TableCommandType> commandPublisher;

    private final LoginRepository loginRepository;

    private final TableRepository tableRepository;

    @Inject
    public TableController(CommandPublisher<TableCommandType> commandPublisher,
            LoginRepository loginRepository, TableRepository tableRepository) {
        this.commandPublisher = commandPublisher;
        this.loginRepository = loginRepository;
        this.tableRepository = tableRepository;
    }

    @SubscribeMapping(value = "/topic/game/{gameId}/table/{tableId}")
    public TableViewModel fetchTable(@DestinationVariable UUID gameId,
            @DestinationVariable UUID tableId) {
        return tableRepository.fetchById(tableId);
    }

    @MessageMapping(value = "/app/check")
    public void check(BaseHandActionViewModel model, Principal principal) {
        UUID gameId = UUID.fromString(model.getGameId());
        UUID tableId = UUID.fromString(model.getTableId());
        UUID playerId = loginRepository.fetchAggregateIdByUsername(principal.getName());
        commandPublisher.publish(new CheckCommand(tableId, gameId, playerId));
    }

    @MessageMapping(value = "/app/fold")
    public void fold(BaseHandActionViewModel model, Principal principal) {
        UUID gameId = UUID.fromString(model.getGameId());
        UUID tableId = UUID.fromString(model.getTableId());
        UUID playerId = loginRepository.fetchAggregateIdByUsername(principal.getName());
        commandPublisher.publish(new FoldCommand(tableId, gameId, playerId));
    }

    @MessageMapping(value = "/app/call")
    public void call(BaseHandActionViewModel model, Principal principal) {
        UUID gameId = UUID.fromString(model.getGameId());
        UUID tableId = UUID.fromString(model.getTableId());
        UUID playerId = loginRepository.fetchAggregateIdByUsername(principal.getName());
        commandPublisher.publish(new CallCommand(tableId, gameId, playerId));
    }

    @MessageMapping(value = "/app/raise")
    public void raise(RaiseHandActionViewModel model, Principal principal) {
        UUID gameId = UUID.fromString(model.getGameId());
        UUID tableId = UUID.fromString(model.getTableId());
        UUID playerId = loginRepository.fetchAggregateIdByUsername(principal.getName());
        commandPublisher.publish(new RaiseCommand(tableId, gameId, playerId, model
                .getRaiseToAmount()));
    }

}
