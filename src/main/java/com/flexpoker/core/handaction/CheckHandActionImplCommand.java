package com.flexpoker.core.handaction;

import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;

import com.flexpoker.core.api.chat.SendTableChatMessageCommand;
import com.flexpoker.core.api.handaction.CheckHandActionCommand;
import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameEventType;
import com.flexpoker.model.Hand;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;
import com.flexpoker.model.chat.outgoing.TableChatMessage;
import com.flexpoker.repository.api.GameRepository;
import com.flexpoker.util.ActionOnSeatPredicate;

public class CheckHandActionImplCommand implements CheckHandActionCommand {

    private final GameRepository gameRepository;
    
    private final SendTableChatMessageCommand sendTableChatMessageCommand;
    
    @Inject
    public CheckHandActionImplCommand(GameRepository gameRepository,
            SendTableChatMessageCommand sendTableChatMessageCommand) {
        this.gameRepository = gameRepository;
        this.sendTableChatMessageCommand = sendTableChatMessageCommand;
    }
    
    @Override
    public void execute(UUID gameId, UUID tableId, User user) {
        Game game = gameRepository.findById(gameId);
        Table table = game.getTable(tableId);
        Hand realTimeHand = table.getCurrentHand();

        if (!isUserAllowedToPerformAction(GameEventType.CHECK, user, realTimeHand, table)) {
            throw new FlexPokerException("Not allowed to check.");
        }

        Seat actionOnSeat = (Seat) CollectionUtils.find(table.getSeats(), new ActionOnSeatPredicate());

        resetAllSeatActions(actionOnSeat, realTimeHand);

        actionOnSeat.setCallAmount(0);
        actionOnSeat.setRaiseTo(0);

        if (actionOnSeat.equals(realTimeHand.getLastToAct())) {
            handleEndOfRound(game, table, realTimeHand, game.getCurrentBlinds().getBigBlind());
        } else {
            handleMiddleOfRound(game, table, realTimeHand, actionOnSeat);
        }

        String message = user.getUsername() + " checks";
        sendTableChatMessageCommand.execute(new TableChatMessage(message, null, true, gameId, tableId));
    }

}
