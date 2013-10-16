package com.flexpoker.bso;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.flexpoker.bso.api.ActionOnTimerBso;
import com.flexpoker.bso.api.PlayerActionsBso;
import com.flexpoker.bso.api.PotBso;
import com.flexpoker.bso.api.ValidationBso;
import com.flexpoker.core.api.handaction.CallHandActionCommand;
import com.flexpoker.core.api.handaction.CheckHandActionCommand;
import com.flexpoker.core.api.handaction.FoldHandActionCommand;
import com.flexpoker.core.api.handaction.RaiseHandActionCommand;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForEndOfHandCommand;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForNewRoundCommand;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameEventType;
import com.flexpoker.model.Hand;
import com.flexpoker.model.HandDealerState;
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.HandRoundState;
import com.flexpoker.model.Pot;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.repository.api.GameRepository;
import com.flexpoker.repository.api.UserRepository;
import com.flexpoker.util.ActionOnSeatPredicate;
import com.flexpoker.util.ButtonSeatPredicate;

@Service
public class PlayerActionsBsoImpl implements PlayerActionsBso {

    private final PotBso potBso;

    private final SetSeatStatusForEndOfHandCommand setSeatStatusForEndOfHandCommand;
    
    private final SetSeatStatusForNewRoundCommand setSeatStatusForNewRoundCommand;

    private final ValidationBso validationBso;

    private final ActionOnTimerBso actionOnTimerBso;
    
    private final GameRepository gameRepository;
    
    private final UserRepository userRepository;
    
    private final CheckHandActionCommand checkHandActionCommand;
    
    private final CallHandActionCommand callHandActionCommand;
    
    private final FoldHandActionCommand foldHandActionCommand;
    
    private final RaiseHandActionCommand raiseHandActionCommand;
    
    @Inject
    public PlayerActionsBsoImpl(
            PotBso potBso,
            SetSeatStatusForEndOfHandCommand setSeatStatusForEndOfHandCommand,
            SetSeatStatusForNewRoundCommand setSeatStatusForNewRoundCommand,
            ValidationBso validationBso,
            ActionOnTimerBso actionOnTimerBso,
            GameRepository gameRepository,
            UserRepository userRepository,
            CheckHandActionCommand checkHandActionCommand,
            CallHandActionCommand callHandActionCommand,
            FoldHandActionCommand foldHandActionCommand,
            RaiseHandActionCommand raiseHandActionCommand) {
        this.potBso = potBso;
        this.setSeatStatusForEndOfHandCommand = setSeatStatusForEndOfHandCommand;
        this.setSeatStatusForNewRoundCommand = setSeatStatusForNewRoundCommand;
        this.validationBso = validationBso;
        this.actionOnTimerBso = actionOnTimerBso;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.checkHandActionCommand = checkHandActionCommand;
        this.callHandActionCommand = callHandActionCommand;
        this.foldHandActionCommand = foldHandActionCommand;
        this.raiseHandActionCommand = raiseHandActionCommand;
    }

    @Override
    public void check(UUID gameId, UUID tableId, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        checkHandActionCommand.execute(gameId, tableId, user);
    }

    @Override
    public void fold(UUID gameId, UUID tableId, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        foldHandActionCommand.execute(gameId, tableId, user);
    }

    @Override
    public void call(UUID gameId, UUID tableId, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        callHandActionCommand.execute(gameId, tableId, user);
    }

    @Override
    public void raise(UUID gameId, UUID tableId, int raiseToAmount, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        raiseHandActionCommand.execute(gameId, tableId, raiseToAmount, user);
    }

    private void handleMiddleOfRound(Game game, Table table,
            Hand realTimeHand, Seat actionOnSeat) {
        realTimeHand.setHandRoundState(HandRoundState.ROUND_IN_PROGRESS);
        actionOnSeat.setActionOn(false);
        actionOnTimerBso.removeSeat(table, actionOnSeat);
        Seat nextToActSeat = realTimeHand.getNextToAct();
        nextToActSeat.setActionOn(true);
        actionOnTimerBso.addSeat(table, nextToActSeat);
        determineNextToAct(table, realTimeHand);
    }

    private void handleEndOfRound(Game game, Table table,
            Hand realTimeHand, int bigBlindAmount) {
        realTimeHand.setOriginatingBettor(null);
        realTimeHand.setHandRoundState(HandRoundState.ROUND_COMPLETE);
        moveToNextHandDealerState(realTimeHand);
        potBso.calculatePotsAfterRound(game, table);
        table.getCurrentHand().setPots(new HashSet<>(potBso.fetchAllPots(game, table)));

        if (realTimeHand.getHandDealerState() == HandDealerState.COMPLETE) {
            setSeatStatusForEndOfHandCommand.execute(table);
            determineWinners(game, table, realTimeHand.getHandEvaluationList());
        } else {
            setSeatStatusForNewRoundCommand.execute(table);
            determineNextToAct(table, realTimeHand);
            determineLastToAct(table, realTimeHand);
            resetRaiseAmountsAfterRound(table, bigBlindAmount);
            resetPossibleSeatActionsAfterRound(table, realTimeHand);
        }
    }

    private void resetRaiseAmountsAfterRound(Table table, int bigBlindAmount) {
        for (Seat seat : table.getSeats()) {
            seat.setCallAmount(0);
            if (bigBlindAmount > seat.getUserGameStatus().getChips()) {
                seat.setRaiseTo(seat.getUserGameStatus().getChips());
            } else {
                seat.setRaiseTo(bigBlindAmount);
            }
        }
    }

    private void resetPossibleSeatActionsAfterRound(Table table,
            Hand realTimeHand) {
        for (Seat seat : table.getSeats()) {
            realTimeHand.addPossibleSeatAction(seat, GameEventType.CHECK);
            realTimeHand.addPossibleSeatAction(seat, GameEventType.RAISE);
            realTimeHand.removePossibleSeatAction(seat, GameEventType.CALL);
            realTimeHand.removePossibleSeatAction(seat, GameEventType.FOLD);
        }
    }

    private void resetAllSeatActions(Seat seat, Hand realTimeHand) {
        realTimeHand.removePossibleSeatAction(seat, GameEventType.CHECK);
        realTimeHand.removePossibleSeatAction(seat, GameEventType.RAISE);
        realTimeHand.removePossibleSeatAction(seat, GameEventType.CALL);
        realTimeHand.removePossibleSeatAction(seat, GameEventType.FOLD);
    }

    private void determineLastToAct(Table table, Hand realTimeHand) {
        List<Seat> seats = table.getSeats();

        int seatIndex;

        if (realTimeHand.getOriginatingBettor() == null) {
            Seat buttonSeat = (Seat) CollectionUtils.find(table.getSeats(),
                    new ButtonSeatPredicate());
            seatIndex = seats.indexOf(buttonSeat);
        } else {
            seatIndex = seats.indexOf(realTimeHand.getOriginatingBettor());
            if (seatIndex == 0) {
                seatIndex = seats.size() - 1;
            } else {
                seatIndex--;
            }
        }

        for (int i = seatIndex; i >= 0; i--) {
            if (seats.get(i).isStillInHand()) {
                realTimeHand.setLastToAct(seats.get(i));
                return;
            }
        }

        for (int i = seats.size() - 1; i > seatIndex; i--) {
            if (seats.get(i).isStillInHand()) {
                realTimeHand.setLastToAct(seats.get(i));
                return;
            }
        }
    }

    private void determineNextToAct(Table table, Hand realTimeHand) {
        List<Seat> seats = table.getSeats();
        Seat actionOnSeat = (Seat) CollectionUtils.find(seats,
                new ActionOnSeatPredicate());

        int actionOnIndex = seats.indexOf(actionOnSeat);

        for (int i = actionOnIndex + 1; i < seats.size(); i++) {
            if (seats.get(i).isStillInHand()) {
                realTimeHand.setNextToAct(seats.get(i));
                return;
            }
        }

        for (int i = 0; i < actionOnIndex; i++) {
            if (seats.get(i).isStillInHand()) {
                realTimeHand.setNextToAct(seats.get(i));
                return;
            }
        }
    }

    private boolean isUserAllowedToPerformAction(GameEventType action,
            User user, Hand realTimeHand, Table table) {

        if (realTimeHand.getHandDealerState() == HandDealerState.COMPLETE) {
            return false;
        }

        Seat usersSeat = null;

        for (Seat seat : table.getSeats()) {
            if (seat.getUserGameStatus() != null
                && user.equals(seat.getUserGameStatus().getUser())) {
                usersSeat = seat;
                break;
            }
        }

        return realTimeHand.isUserAllowedToPerformAction(action, usersSeat);
    }

    private void moveToNextHandDealerState(Hand realTimeHand) {
        HandDealerState handDealerState = realTimeHand.getHandDealerState();

        switch (handDealerState) {
            case POCKET_CARDS_DEALT:
                realTimeHand.setHandDealerState(HandDealerState.FLOP_DEALT);
                break;
            case FLOP_DEALT:
                realTimeHand.setHandDealerState(HandDealerState.TURN_DEALT);
                break;
            case TURN_DEALT:
                realTimeHand.setHandDealerState(HandDealerState.RIVER_DEALT);
                break;
            case RIVER_DEALT:
                realTimeHand.setHandDealerState(HandDealerState.COMPLETE);
                break;
            case COMPLETE:
                break;
            default:
                throw new IllegalStateException("No valid state to move to.");
        }
    }

    private void determineWinners(Game game, Table table, List<HandEvaluation> handEvaluationList) {
        potBso.setWinners(game, table, handEvaluationList);

        for (Pot pot : potBso.fetchAllPots(game, table)) {
            List<Seat> winners = pot.getWinners();
            int numberOfWinners = winners.size();
            int numberOfChips = pot.getAmount() / numberOfWinners;
            int bonusChips = pot.getAmount() % numberOfWinners;
            int numberOfPlayersInPot = pot.getSeats().size();

            winners.get(0).getUserGameStatus().setChips(
                    winners.get(0).getUserGameStatus().getChips() + bonusChips);

            for (Seat winner : winners) {
                winner.getUserGameStatus().setChips(
                        winner.getUserGameStatus().getChips() + numberOfChips);
                PocketCards pocketCards = table.getCurrentHand().getDeck()
                        .getPocketCards(winner.getPosition());
                if (numberOfPlayersInPot > 1) {
                    winner.setShowCards(pocketCards);
                }
            }
        }
    }

}
