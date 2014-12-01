package com.flexpoker.table.query.handlers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.core.api.chat.SendTableChatMessageCommand;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.login.query.repository.LoginRepository;
import com.flexpoker.model.chat.outgoing.TableChatMessage;
import com.flexpoker.table.command.events.PlayerCheckedEvent;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.model.table.TableViewModel;

@Component
public class PlayerCheckedEventHandler implements EventHandler<PlayerCheckedEvent> {

    private final LoginRepository loginRepository;

    private final TableRepository tableRepository;

    private final SendTableChatMessageCommand sendTableChatMessageCommand;

    @Inject
    public PlayerCheckedEventHandler(LoginRepository loginRepository,
            TableRepository tableRepository,
            SendTableChatMessageCommand sendTableChatMessageCommand) {
        this.loginRepository = loginRepository;
        this.tableRepository = tableRepository;
        this.sendTableChatMessageCommand = sendTableChatMessageCommand;
    }

    @Async
    @Override
    public void handle(PlayerCheckedEvent event) {
        TableViewModel tableDTO = tableRepository.fetchById(event.getAggregateId());
        // Game game = gameRepository.findById(gameId);
        // Table table = game.getTable(tableId);
        // Hand realTimeHand = table.getCurrentHand();

        // Seat actionOnSeat = table.getActionOnSeat();

        // realTimeHand.resetPlayerActions(actionOnSeat);

        // actionOnSeat.setCallAmount(0);
        // actionOnSeat.setRaiseTo(0);

        // if (actionOnSeat.equals(realTimeHand.getLastToAct())) {
        // handleEndOfRound(game, table, realTimeHand, game.getCurrentBlinds()
        // .getBigBlind());
        // } else {
        // handleMiddleOfRound(game, table, realTimeHand, actionOnSeat);
        // }

        handleChat(event);
    }

    private void handleChat(PlayerCheckedEvent event) {
        String message = loginRepository.fetchUsernameByAggregateId(event.getPlayerId())
                + " checks";
        sendTableChatMessageCommand.execute(new TableChatMessage(message, null, true,
                event.getGameId(), event.getAggregateId()));

    }
}
