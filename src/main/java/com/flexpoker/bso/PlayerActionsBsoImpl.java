package com.flexpoker.bso;

import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.flexpoker.bso.api.ActionOnTimerBso;
import com.flexpoker.bso.api.DeckBso;
import com.flexpoker.bso.api.PlayerActionsBso;
import com.flexpoker.bso.api.PotBso;
import com.flexpoker.bso.api.ValidationBso;
import com.flexpoker.core.api.game.ChangeGameStageCommand;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForEndOfHandCommand;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForNewRoundCommand;
import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.Blinds;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameEventType;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.Hand;
import com.flexpoker.model.HandDealerState;
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.HandRoundState;
import com.flexpoker.model.HandState;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.Pot;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;
import com.flexpoker.model.UserGameStatus;
import com.flexpoker.util.ActionOnSeatPredicate;
import com.flexpoker.util.ButtonSeatPredicate;

@Service
public class PlayerActionsBsoImpl implements PlayerActionsBso {

    private final PotBso potBso;

    private final SetSeatStatusForEndOfHandCommand setSeatStatusForEndOfHandCommand;
    
    private final SetSeatStatusForNewRoundCommand setSeatStatusForNewRoundCommand;

    private final ValidationBso validationBso;

    private final DeckBso deckBso;

    private final ActionOnTimerBso actionOnTimerBso;
    
    private final ChangeGameStageCommand changeGameStageCommand;
    
    @Inject
    public PlayerActionsBsoImpl(
            PotBso potBso,
            SetSeatStatusForEndOfHandCommand setSeatStatusForEndOfHandCommand,
            SetSeatStatusForNewRoundCommand setSeatStatusForNewRoundCommand,
            ValidationBso validationBso,
            DeckBso deckBso,
            ActionOnTimerBso actionOnTimerBso,
            ChangeGameStageCommand changeGameStageCommand) {
        this.potBso = potBso;
        this.setSeatStatusForEndOfHandCommand = setSeatStatusForEndOfHandCommand;
        this.setSeatStatusForNewRoundCommand = setSeatStatusForNewRoundCommand;
        this.validationBso = validationBso;
        this.deckBso = deckBso;
        this.actionOnTimerBso = actionOnTimerBso;
        this.changeGameStageCommand = changeGameStageCommand;
    }

    @Override
    public HandState check(Game game, Table table, User user) {
        synchronized (this) {
            Hand realTimeHand = table.getCurrentHand();
            table = game.getTable(table.getId());

            if (!isUserAllowedToPerformAction(GameEventType.CHECK, user,
                    realTimeHand, table)) {
                throw new FlexPokerException("Not allowed to check.");
            }

            Seat actionOnSeat = (Seat) CollectionUtils.find(table.getSeats(),
                    new ActionOnSeatPredicate());

            resetAllSeatActions(actionOnSeat, realTimeHand);

            actionOnSeat.setCallAmount(0);
            actionOnSeat.setRaiseTo(0);

            if (actionOnSeat.equals(realTimeHand.getLastToAct())) {
                handleEndOfRound(game, table, realTimeHand,
                        game.getCurrentBlinds().getBigBlind());
            } else {
                handleMiddleOfRound(game, table, realTimeHand, actionOnSeat);
            }

            return new HandState(realTimeHand.getHandDealerState(),
                    realTimeHand.getHandRoundState());
        }
    }

    @Override
    public HandState fold(Game game, Table table, User user) {
        synchronized (this) {
            Game realTimeGame = game;
            Hand realTimeHand = table.getCurrentHand();
            table = realTimeGame.getTable(table.getId());

            if (!isUserAllowedToPerformAction(GameEventType.FOLD, user,
                    realTimeHand, table)) {
                throw new FlexPokerException("Not allowed to fold.");
            }

            Seat actionOnSeat = (Seat) CollectionUtils.find(table.getSeats(),
                    new ActionOnSeatPredicate());

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
                handleEndOfRound(game, table, realTimeHand,
                        realTimeGame.getCurrentBlinds().getBigBlind());
            } else if (actionOnSeat.equals(realTimeHand.getLastToAct())) {
                handleEndOfRound(game, table, realTimeHand,
                        realTimeGame.getCurrentBlinds().getBigBlind());
            } else {
                handleMiddleOfRound(game, table, realTimeHand, actionOnSeat);
            }

            return new HandState(realTimeHand.getHandDealerState(),
                    realTimeHand.getHandRoundState());
        }
    }

    @Override
    public HandState call(Game game, Table table, User user) {
        synchronized (this) {
            Hand realTimeHand = table.getCurrentHand();
            table = game.getTable(table.getId());

            Seat actionOnSeat = (Seat) CollectionUtils.find(table.getSeats(),
                    new ActionOnSeatPredicate());

            if (!isUserAllowedToPerformAction(GameEventType.CALL, user,
                    realTimeHand, table)) {
                throw new FlexPokerException("Not allowed to call.");
            }

            resetAllSeatActions(actionOnSeat, realTimeHand);

            actionOnSeat.setChipsInFront(actionOnSeat.getChipsInFront()
                    + actionOnSeat.getCallAmount());

            UserGameStatus userGameStatus = actionOnSeat.getUserGameStatus();
            userGameStatus.setChips(userGameStatus.getChips() - actionOnSeat.getCallAmount());
            table.getCurrentHand().addToTotalPot(actionOnSeat.getCallAmount());

            actionOnSeat.setCallAmount(0);
            actionOnSeat.setRaiseTo(0);

            if (actionOnSeat.equals(realTimeHand.getLastToAct())) {
                handleEndOfRound(game, table, realTimeHand,
                        game.getCurrentBlinds().getBigBlind());
            } else {
                handleMiddleOfRound(game, table, realTimeHand, actionOnSeat);
            }

            int numberOfPlayersLeft = 0;
            for (Seat seat : table.getSeats()) {
                if (seat.getUserGameStatus() != null
                        && seat.getUserGameStatus().getChips() != 0) {
                    numberOfPlayersLeft++;
                }
            }

            // TODO: This should be a check with all of the tables in the game.
            // TODO: This check should also be done at the beginning of the
            //       hand in case the player does not have enough to call the
            //       blinds.
            if (numberOfPlayersLeft == 1) {
                changeGameStageCommand.execute(game.getId(), GameStage.FINISHED);
            }

            return new HandState(realTimeHand.getHandDealerState(),
                    realTimeHand.getHandRoundState());
        }
    }

    @Override
    public HandState raise(Game game, Table table, User user, String raiseAmount) {
        synchronized (this) {
            Hand realTimeHand = table.getCurrentHand();
            table = game.getTable(table.getId());

            Blinds currentBlinds = game.getCurrentBlinds();
            int bigBlind = currentBlinds.getBigBlind();

            Seat actionOnSeat = (Seat) CollectionUtils.find(table.getSeats(),
                    new ActionOnSeatPredicate());

            if (!isUserAllowedToPerformAction(GameEventType.RAISE, user,
                    realTimeHand, table)) {
                throw new FlexPokerException("Not allowed to raise.");
            }

            validationBso.validateRaiseAmount(actionOnSeat.getRaiseTo(),
                    actionOnSeat.getUserGameStatus().getChips(), raiseAmount);

            int raiseAmountInt = Integer.parseInt(raiseAmount);
            int raiseAboveCall = raiseAmountInt -
                    (actionOnSeat.getChipsInFront() + actionOnSeat.getCallAmount());
            int increaseOfChipsInFront = raiseAmountInt - actionOnSeat.getChipsInFront();

            realTimeHand.setOriginatingBettor(actionOnSeat);
            determineLastToAct(table, realTimeHand);
            handleMiddleOfRound(game, table, realTimeHand, actionOnSeat);
            actionOnSeat.setChipsInFront(raiseAmountInt);

            UserGameStatus userGameStatus = actionOnSeat.getUserGameStatus();
            userGameStatus.setChips(userGameStatus.getChips() - increaseOfChipsInFront);
            table.getCurrentHand().addToTotalPot(increaseOfChipsInFront);

            realTimeHand.addPossibleSeatAction(actionOnSeat, GameEventType.CALL);
            realTimeHand.addPossibleSeatAction(actionOnSeat, GameEventType.RAISE);
            realTimeHand.addPossibleSeatAction(actionOnSeat, GameEventType.FOLD);
            realTimeHand.removePossibleSeatAction(actionOnSeat, GameEventType.CHECK);

            for (Seat seat : table.getSeats()) {
                if (seat.isStillInHand() && !actionOnSeat.equals(seat)) {
                    int totalChips = seat.getUserGameStatus().getChips()
                            + seat.getChipsInFront();
                    realTimeHand.addPossibleSeatAction(seat, GameEventType.CALL);
                    realTimeHand.addPossibleSeatAction(seat, GameEventType.FOLD);
                    realTimeHand.removePossibleSeatAction(seat, GameEventType.CHECK);
                    if (totalChips < raiseAmountInt) {
                        seat.setCallAmount(totalChips);
                        seat.setRaiseTo(0);
                        realTimeHand.removePossibleSeatAction(seat, GameEventType.RAISE);
                    } else {
                        seat.setCallAmount(raiseAmountInt - seat.getChipsInFront());
                        realTimeHand.addPossibleSeatAction(seat, GameEventType.RAISE);
                        if (totalChips < raiseAmountInt + raiseAboveCall) {
                            seat.setRaiseTo(totalChips);
                        } else {
                            seat.setRaiseTo(raiseAmountInt + raiseAboveCall);
                        }
                    }
                }
            }

            actionOnSeat.setCallAmount(0);
            actionOnSeat.setRaiseTo(bigBlind);

            return new HandState(realTimeHand.getHandDealerState(),
                    realTimeHand.getHandRoundState());
        }
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
                PocketCards pocketCards = deckBso.fetchPocketCards(
                        winner.getUserGameStatus().getUser(), game, table);
                if (numberOfPlayersInPot > 1) {
                    winner.setShowCards(pocketCards);
                }
            }
        }
    }
    
}
