package com.flexpoker.core.handaction;

import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;

import com.flexpoker.core.api.chat.SendTableChatMessageCommand;
import com.flexpoker.core.api.handaction.RaiseHandActionCommand;
import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.Blinds;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameEventType;
import com.flexpoker.model.Hand;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;
import com.flexpoker.model.UserGameStatus;
import com.flexpoker.model.chat.outgoing.TableChatMessage;
import com.flexpoker.repository.api.GameRepository;
import com.flexpoker.util.ActionOnSeatPredicate;

public class RaiseHandActionImplCommand implements RaiseHandActionCommand {

    private final GameRepository gameRepository;
    
    private final SendTableChatMessageCommand sendTableChatMessageCommand;
    
    @Inject
    public RaiseHandActionImplCommand(GameRepository gameRepository,
            SendTableChatMessageCommand sendTableChatMessageCommand) {
        this.gameRepository = gameRepository;
        this.sendTableChatMessageCommand = sendTableChatMessageCommand;
    }
    
    @Override
    public void execute(UUID gameId, UUID tableId, int raiseToAmount, User user) {
        Game game = gameRepository.findById(gameId);
        Table table = game.getTable(tableId);
        Hand realTimeHand = table.getCurrentHand();

        Blinds currentBlinds = game.getCurrentBlinds();
        int bigBlind = currentBlinds.getBigBlind();

        Seat actionOnSeat = (Seat) CollectionUtils.find(table.getSeats(), new ActionOnSeatPredicate());

        if (!isUserAllowedToPerformAction(GameEventType.RAISE, user, realTimeHand, table)) {
            throw new FlexPokerException("Not allowed to raise.");
        }

        validationBso.validateRaiseAmount(actionOnSeat.getRaiseTo(),
                actionOnSeat.getUserGameStatus().getChips(), raiseToAmount);

        int raiseAboveCall = raiseToAmount - (actionOnSeat.getChipsInFront() + actionOnSeat.getCallAmount());
        int increaseOfChipsInFront = raiseToAmount - actionOnSeat.getChipsInFront();

        realTimeHand.setOriginatingBettor(actionOnSeat);
        determineLastToAct(table, realTimeHand);
        handleMiddleOfRound(game, table, realTimeHand, actionOnSeat);
        actionOnSeat.setChipsInFront(raiseToAmount);

        UserGameStatus userGameStatus = actionOnSeat.getUserGameStatus();
        userGameStatus.setChips(userGameStatus.getChips() - increaseOfChipsInFront);
        table.getCurrentHand().addToTotalPot(increaseOfChipsInFront);

        realTimeHand.addPossibleSeatAction(actionOnSeat, GameEventType.CALL);
        realTimeHand.addPossibleSeatAction(actionOnSeat, GameEventType.RAISE);
        realTimeHand.addPossibleSeatAction(actionOnSeat, GameEventType.FOLD);
        realTimeHand.removePossibleSeatAction(actionOnSeat, GameEventType.CHECK);

        for (Seat seat : table.getSeats()) {
            if (seat.isStillInHand() && !actionOnSeat.equals(seat)) {
                int totalChips = seat.getUserGameStatus().getChips() + seat.getChipsInFront();
                realTimeHand.addPossibleSeatAction(seat, GameEventType.CALL);
                realTimeHand.addPossibleSeatAction(seat, GameEventType.FOLD);
                realTimeHand.removePossibleSeatAction(seat, GameEventType.CHECK);
                if (totalChips < raiseToAmount) {
                    seat.setCallAmount(totalChips);
                    seat.setRaiseTo(0);
                    realTimeHand.removePossibleSeatAction(seat, GameEventType.RAISE);
                } else {
                    seat.setCallAmount(raiseToAmount - seat.getChipsInFront());
                    realTimeHand.addPossibleSeatAction(seat, GameEventType.RAISE);
                    if (totalChips < raiseToAmount + raiseAboveCall) {
                        seat.setRaiseTo(totalChips);
                    } else {
                        seat.setRaiseTo(raiseToAmount + raiseAboveCall);
                    }
                }
            }
        }

        actionOnSeat.setCallAmount(0);
        actionOnSeat.setRaiseTo(bigBlind);

        String message = user.getUsername() + " raises to " + raiseToAmount;
        sendTableChatMessageCommand.execute(new TableChatMessage(message, null, true, gameId, tableId));
    }

}
