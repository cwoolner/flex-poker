package com.flexpoker.core.handaction;

import java.util.UUID;

import javax.inject.Inject;

import org.springframework.context.ApplicationEventPublisher;

import com.flexpoker.config.Command;
import com.flexpoker.core.api.chat.SendTableChatMessageCommand;
import com.flexpoker.core.api.game.StartNewHandCommand;
import com.flexpoker.core.api.handaction.CallHandActionCommand;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForEndOfHandCommand;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForNewHandCommand;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForNewRoundCommand;
import com.flexpoker.core.pot.CalculatePotsAfterRoundImplQuery;
import com.flexpoker.core.pot.DeterminePotWinnersImplQuery;
import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.Hand;
import com.flexpoker.model.PlayerAction;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;
import com.flexpoker.model.UserGameStatus;
import com.flexpoker.model.chat.outgoing.TableChatMessage;
import com.flexpoker.repository.api.GameRepository;

@Command
public class CallHandActionImplCommand extends BaseHandActionCommand
    implements CallHandActionCommand {

    @Inject
    public CallHandActionImplCommand(GameRepository gameRepository,
            SendTableChatMessageCommand sendTableChatMessageCommand,
            SetSeatStatusForEndOfHandCommand setSeatStatusForEndOfHandCommand,
            SetSeatStatusForNewRoundCommand setSeatStatusForNewRoundCommand,
            SetSeatStatusForNewHandCommand setSeatStatusForNewHandCommand,
            CalculatePotsAfterRoundImplQuery calculatePotsAfterRoundImplQuery,    
            DeterminePotWinnersImplQuery determinePotWinnersImplQuery,
            StartNewHandCommand startNewHandCommand,
            ApplicationEventPublisher applicationEventPublisher) {
        this.gameRepository = gameRepository;
        this.sendTableChatMessageCommand = sendTableChatMessageCommand;
        this.setSeatStatusForEndOfHandCommand = setSeatStatusForEndOfHandCommand;
        this.setSeatStatusForNewRoundCommand = setSeatStatusForNewRoundCommand;
        this.calculatePotsAfterRoundImplQuery = calculatePotsAfterRoundImplQuery;
        this.determinePotWinnersImplQuery = determinePotWinnersImplQuery;
        this.startNewHandCommand = startNewHandCommand;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void execute(UUID gameId, UUID tableId, User user) {
        Game game = gameRepository.findById(gameId);
        Table table = game.getTable(tableId);
        Hand realTimeHand = table.getCurrentHand();

        Seat actionOnSeat = table.getActionOnSeat();
        
        if (!actionOnSeat.getUserGameStatus().getUser().equals(user)
                || !realTimeHand.isUserAllowedToPerformAction(PlayerAction.CALL, actionOnSeat))
        {
            throw new FlexPokerException("Not allowed to call.");
        }

        realTimeHand.resetPlayerActions(actionOnSeat);

        actionOnSeat.setChipsInFront(actionOnSeat.getChipsInFront() + actionOnSeat.getCallAmount());

        UserGameStatus userGameStatus = actionOnSeat.getUserGameStatus();
        userGameStatus.removeChips(actionOnSeat.getCallAmount());
        table.getCurrentHand().addToTotalPot(actionOnSeat.getCallAmount());

        actionOnSeat.setCallAmount(0);
        actionOnSeat.setRaiseTo(0);

        if (actionOnSeat.equals(realTimeHand.getLastToAct())) {
            handleEndOfRound(game, table, realTimeHand, game.getCurrentBlinds().getBigBlind());
        } else {
            handleMiddleOfRound(game, table, realTimeHand, actionOnSeat);
        }

        int numberOfPlayersLeft = 0;
        for (Seat seat : table.getSeats()) {
            if (seat.getUserGameStatus() != null && seat.getUserGameStatus().getChips() != 0) {
                numberOfPlayersLeft++;
            }
        }

        // TODO: This should be a check with all of the tables in the game.
        // TODO: This check should also be done at the beginning of the
        //       hand in case the player does not have enough to call the
        //       blinds.
        if (numberOfPlayersLeft == 1) {
            game.setGameStage(GameStage.FINISHED);
        }
        
        String message = user.getUsername() + " calls";
        sendTableChatMessageCommand.execute(new TableChatMessage(message, null, true, gameId, tableId));
    }

}
