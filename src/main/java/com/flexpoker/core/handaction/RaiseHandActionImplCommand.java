package com.flexpoker.core.handaction;

import java.util.UUID;

import javax.inject.Inject;

import com.flexpoker.config.Command;
import com.flexpoker.core.api.actionon.CreateAndStartActionOnTimerCommand;
import com.flexpoker.core.api.chat.SendTableChatMessageCommand;
import com.flexpoker.core.api.game.StartNewHandCommand;
import com.flexpoker.core.api.handaction.RaiseHandActionCommand;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForEndOfHandCommand;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForNewHandCommand;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForNewRoundCommand;
import com.flexpoker.core.pot.CalculatePotsAfterRoundImplQuery;
import com.flexpoker.core.pot.DeterminePotWinnersImplQuery;
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

@Command
public class RaiseHandActionImplCommand extends BaseHandActionCommand
    implements RaiseHandActionCommand {

    @Inject
    public RaiseHandActionImplCommand(GameRepository gameRepository,
            SendTableChatMessageCommand sendTableChatMessageCommand,
            SetSeatStatusForEndOfHandCommand setSeatStatusForEndOfHandCommand,
            SetSeatStatusForNewRoundCommand setSeatStatusForNewRoundCommand,
            SetSeatStatusForNewHandCommand setSeatStatusForNewHandCommand,
            CalculatePotsAfterRoundImplQuery calculatePotsAfterRoundImplQuery,    
            DeterminePotWinnersImplQuery determinePotWinnersImplQuery,
            CreateAndStartActionOnTimerCommand createAndStartActionOnTimerCommand,
            StartNewHandCommand startNewHandCommand) {
        this.gameRepository = gameRepository;
        this.sendTableChatMessageCommand = sendTableChatMessageCommand;
        this.setSeatStatusForEndOfHandCommand = setSeatStatusForEndOfHandCommand;
        this.setSeatStatusForNewRoundCommand = setSeatStatusForNewRoundCommand;
        this.setSeatStatusForNewHandCommand = setSeatStatusForNewHandCommand;
        this.calculatePotsAfterRoundImplQuery = calculatePotsAfterRoundImplQuery;
        this.determinePotWinnersImplQuery = determinePotWinnersImplQuery;
        this.createAndStartActionOnTimerCommand = createAndStartActionOnTimerCommand;
        this.startNewHandCommand = startNewHandCommand;
    }
    
    @Override
    public void execute(UUID gameId, UUID tableId, int raiseToAmount, User user) {
        Game game = gameRepository.findById(gameId);
        Table table = game.getTable(tableId);
        Hand realTimeHand = table.getCurrentHand();

        Blinds currentBlinds = game.getCurrentBlinds();
        int bigBlind = currentBlinds.getBigBlind();

        Seat actionOnSeat = table.getActionOnSeat();
        
        if (!actionOnSeat.getUserGameStatus().getUser().equals(user)
                || !realTimeHand.isUserAllowedToPerformAction(GameEventType.RAISE, actionOnSeat))
        {
            throw new FlexPokerException("Not allowed to raise.");
        }

        if (raiseToAmount < actionOnSeat.getRaiseTo()
                || raiseToAmount > actionOnSeat.getUserGameStatus().getChips()) {
            throw new IllegalArgumentException("Raise amount must be between the "
                    + "minimum and maximum values.");
        }
        
        int raiseAboveCall = raiseToAmount - (actionOnSeat.getChipsInFront() + actionOnSeat.getCallAmount());
        int increaseOfChipsInFront = raiseToAmount - actionOnSeat.getChipsInFront();

        realTimeHand.setOriginatingBettor(actionOnSeat);
        determineLastToAct(table, realTimeHand);
        handleMiddleOfRound(game, table, realTimeHand, actionOnSeat);
        actionOnSeat.setChipsInFront(raiseToAmount);

        UserGameStatus userGameStatus = actionOnSeat.getUserGameStatus();
        userGameStatus.removeChips(increaseOfChipsInFront);
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
