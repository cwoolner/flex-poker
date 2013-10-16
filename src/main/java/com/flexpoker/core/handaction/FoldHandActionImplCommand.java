package com.flexpoker.core.handaction;

import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;

import com.flexpoker.core.api.chat.SendTableChatMessageCommand;
import com.flexpoker.core.api.handaction.FoldHandActionCommand;
import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameEventType;
import com.flexpoker.model.Hand;
import com.flexpoker.model.HandDealerState;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;
import com.flexpoker.model.chat.outgoing.TableChatMessage;
import com.flexpoker.repository.api.GameRepository;
import com.flexpoker.util.ActionOnSeatPredicate;

public class FoldHandActionImplCommand implements FoldHandActionCommand {

    private final GameRepository gameRepository;
    
    private final SendTableChatMessageCommand sendTableChatMessageCommand;
    
    @Inject
    public FoldHandActionImplCommand(GameRepository gameRepository,
            SendTableChatMessageCommand sendTableChatMessageCommand) {
        this.gameRepository = gameRepository;
        this.sendTableChatMessageCommand = sendTableChatMessageCommand;
    }
    
    @Override
    public void execute(UUID gameId, UUID tableId, User user) {
        Game game = gameRepository.findById(gameId);
        Table table = game.getTable(tableId);
        Hand realTimeHand = table.getCurrentHand();

        if (!isUserAllowedToPerformAction(GameEventType.FOLD, user, realTimeHand, table)) {
            throw new FlexPokerException("Not allowed to fold.");
        }

        Seat actionOnSeat = (Seat) CollectionUtils.find(table.getSeats(), new ActionOnSeatPredicate());

        actionOnSeat.setStillInHand(false);
        potBso.removeSeatFromPots(game, table, actionOnSeat);
        actionOnTimerBso.removeSeat(table, actionOnSeat);

        resetAllSeatActions(actionOnSeat, realTimeHand);

        actionOnSeat.setCallAmount(0);
        actionOnSeat.setRaiseTo(0);

        int numberOfPlayersLeft = 0;
        for (Seat seat : table.getSeats()) {
            if (seat.isStillInHand()) {
                numberOfPlayersLeft++;
            }
        }

        if (numberOfPlayersLeft == 1) {
            realTimeHand.setHandDealerState(HandDealerState.COMPLETE);
            handleEndOfRound(game, table, realTimeHand, game.getCurrentBlinds().getBigBlind());
        } else if (actionOnSeat.equals(realTimeHand.getLastToAct())) {
            handleEndOfRound(game, table, realTimeHand, game.getCurrentBlinds().getBigBlind());
        } else {
            handleMiddleOfRound(game, table, realTimeHand, actionOnSeat);
        }
        
        String message = user.getUsername() + " folds";
        sendTableChatMessageCommand.execute(new TableChatMessage(message, null, true, gameId, tableId));
    }

}
