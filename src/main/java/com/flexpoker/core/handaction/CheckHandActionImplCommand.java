package com.flexpoker.core.handaction;

import java.util.UUID;

import javax.inject.Inject;

import org.springframework.context.ApplicationEventPublisher;

import com.flexpoker.config.Command;
import com.flexpoker.core.api.chat.SendTableChatMessageCommand;
import com.flexpoker.core.api.game.StartNewHandCommand;
import com.flexpoker.core.api.handaction.CheckHandActionCommand;
import com.flexpoker.core.api.scheduling.ScheduleAndReturnActionOnTimerCommand;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForEndOfHandCommand;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForNewHandCommand;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForNewRoundCommand;
import com.flexpoker.core.pot.CalculatePotsAfterRoundImplQuery;
import com.flexpoker.core.pot.DeterminePotWinnersImplQuery;
import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.Game;
import com.flexpoker.model.Hand;
import com.flexpoker.model.PlayerAction;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;
import com.flexpoker.model.chat.outgoing.TableChatMessage;
import com.flexpoker.repository.api.GameRepository;

@Command
public class CheckHandActionImplCommand extends BaseHandActionCommand
    implements CheckHandActionCommand {

    @Inject
    public CheckHandActionImplCommand(GameRepository gameRepository,
            SendTableChatMessageCommand sendTableChatMessageCommand,
            SetSeatStatusForEndOfHandCommand setSeatStatusForEndOfHandCommand,
            SetSeatStatusForNewRoundCommand setSeatStatusForNewRoundCommand,
            SetSeatStatusForNewHandCommand setSeatStatusForNewHandCommand,
            CalculatePotsAfterRoundImplQuery calculatePotsAfterRoundImplQuery,    
            DeterminePotWinnersImplQuery determinePotWinnersImplQuery,
            ScheduleAndReturnActionOnTimerCommand scheduleAndReturnActionOnTimerCommand,
            StartNewHandCommand startNewHandCommand,
            ApplicationEventPublisher applicationEventPublisher) {
        this.gameRepository = gameRepository;
        this.sendTableChatMessageCommand = sendTableChatMessageCommand;
        this.setSeatStatusForEndOfHandCommand = setSeatStatusForEndOfHandCommand;
        this.setSeatStatusForNewRoundCommand = setSeatStatusForNewRoundCommand;
        this.setSeatStatusForNewHandCommand = setSeatStatusForNewHandCommand;
        this.calculatePotsAfterRoundImplQuery = calculatePotsAfterRoundImplQuery;
        this.determinePotWinnersImplQuery = determinePotWinnersImplQuery;
        this.scheduleAndReturnActionOnTimerCommand = scheduleAndReturnActionOnTimerCommand;
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
                || !realTimeHand.isUserAllowedToPerformAction(PlayerAction.CHECK, actionOnSeat))
        {
            throw new FlexPokerException("Not allowed to check.");
        }
        
        realTimeHand.resetPlayerActions(actionOnSeat);

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
