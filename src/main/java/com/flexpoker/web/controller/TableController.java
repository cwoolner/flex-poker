package com.flexpoker.web.controller;

import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.login.repository.LoginRepository;
import com.flexpoker.table.command.commands.CallCommand;
import com.flexpoker.table.command.commands.CheckCommand;
import com.flexpoker.table.command.commands.FoldCommand;
import com.flexpoker.table.command.commands.RaiseCommand;
import com.flexpoker.table.command.commands.TableCommand;
import com.flexpoker.table.query.dto.PocketCardsDTO;
import com.flexpoker.table.query.dto.TableDTO;
import com.flexpoker.table.query.repository.CardsUsedInHandRepository;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.dto.CallTableActionDTO;
import com.flexpoker.web.dto.CheckTableActionDTO;
import com.flexpoker.web.dto.FoldTableActionDTO;
import com.flexpoker.web.dto.RaiseTableActionDTO;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class TableController {

    private final CommandSender<TableCommand> commandSender;

    private final LoginRepository loginRepository;

    private final TableRepository tableRepository;

    private final CardsUsedInHandRepository cardsUsedInHandRepository;

    @Inject
    public TableController(CommandSender<TableCommand> commandSender,
            LoginRepository loginRepository, TableRepository tableRepository,
            CardsUsedInHandRepository cardsUsedInHandRepository) {
        this.commandSender = commandSender;
        this.loginRepository = loginRepository;
        this.tableRepository = tableRepository;
        this.cardsUsedInHandRepository = cardsUsedInHandRepository;
    }

    @SubscribeMapping("/user/queue/pocketcards")
    public List<PocketCardsDTO> fetchPocketCards(Principal principal) {
        var playerId = loginRepository.fetchAggregateIdByUsername(principal.getName());
        var pocketCardsForUser = cardsUsedInHandRepository.fetchAllPocketCardsForUser(playerId);
        return pocketCardsForUser.entrySet().stream()
                .map(x -> new PocketCardsDTO(x.getKey(), x.getValue().getCard1().getId(), x.getValue().getCard2().getId()))
                .collect(Collectors.toList());
    }

    @SubscribeMapping("/topic/game/{gameId}/table/{tableId}")
    public TableDTO fetchTable(@DestinationVariable UUID gameId,
                               @DestinationVariable UUID tableId) {
        return tableRepository.fetchById(tableId);
    }

    @MessageMapping("/app/check")
    public void check(CheckTableActionDTO model, Principal principal) {
        var playerId = loginRepository.fetchAggregateIdByUsername(principal.getName());
        commandSender.send(new CheckCommand(model.getTableId(), model.getGameId(),
                playerId));
    }

    @MessageMapping("/app/fold")
    public void fold(FoldTableActionDTO model, Principal principal) {
        var playerId = loginRepository.fetchAggregateIdByUsername(principal.getName());
        commandSender.send(new FoldCommand(model.getTableId(), model.getGameId(),
                playerId));
    }

    @MessageMapping("/app/call")
    public void call(CallTableActionDTO model, Principal principal) {
        var playerId = loginRepository.fetchAggregateIdByUsername(principal.getName());
        commandSender.send(new CallCommand(model.getTableId(), model.getTableId(),
                playerId));
    }

    @MessageMapping("/app/raise")
    public void raise(RaiseTableActionDTO model, Principal principal) {
        var playerId = loginRepository.fetchAggregateIdByUsername(principal.getName());
        commandSender.send(new RaiseCommand(model.getTableId(), model.getGameId(),
                playerId, model.getRaiseToAmount()));
    }

}
