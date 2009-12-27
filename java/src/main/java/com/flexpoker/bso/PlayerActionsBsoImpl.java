package com.flexpoker.bso;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.Blinds;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameEventType;
import com.flexpoker.model.HandDealerState;
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.HandRoundState;
import com.flexpoker.model.HandState;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.Pot;
import com.flexpoker.model.RealTimeGame;
import com.flexpoker.model.RealTimeHand;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;
import com.flexpoker.model.UserGameStatus;
import com.flexpoker.util.ActionOnSeatPredicate;
import com.flexpoker.util.ButtonSeatPredicate;

@Service("playerActionsBso")
public class PlayerActionsBsoImpl implements PlayerActionsBso {

    private RealTimeGameBso realTimeGameBso;

    private PotBso potBso;

    private SeatStatusBso seatStatusBso;

    private ValidationBso validationBso;

    private DeckBso deckBso;

    @Override
    public HandState check(Game game, Table table, User user) {
        synchronized (this) {
            RealTimeGame realTimeGame = realTimeGameBso.get(game);
            RealTimeHand realTimeHand = realTimeGame.getRealTimeHand(table);
            table = realTimeGame.getTable(table);

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
                        realTimeGame.getCurrentBlinds().getBigBlind());
            } else {
                handleMiddleOfRound(table, realTimeHand, actionOnSeat);
            }

            return new HandState(realTimeHand.getHandDealerState(),
                    realTimeHand.getHandRoundState());
        }
    }

    @Override
    public HandState fold(Game game, Table table, User user) {
        synchronized (this) {
            RealTimeGame realTimeGame = realTimeGameBso.get(game);
            RealTimeHand realTimeHand = realTimeGame.getRealTimeHand(table);
            table = realTimeGame.getTable(table);

            if (!isUserAllowedToPerformAction(GameEventType.FOLD, user,
                    realTimeHand, table)) {
                throw new FlexPokerException("Not allowed to fold.");
            }

            Seat actionOnSeat = (Seat) CollectionUtils.find(table.getSeats(),
                    new ActionOnSeatPredicate());

            actionOnSeat.setStillInHand(false);

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
                realTimeHand.setOriginatingBettor(null);
                realTimeHand.setHandRoundState(HandRoundState.ROUND_COMPLETE);
                realTimeHand.setHandDealerState(HandDealerState.COMPLETE);
                potBso.calculatePotsAfterRound(game, table);
                determineTablePotAmounts(game, table);
            } else if (actionOnSeat.equals(realTimeHand.getLastToAct())) {
                handleEndOfRound(game, table, realTimeHand,
                        realTimeGame.getCurrentBlinds().getBigBlind());
            } else {
                handleMiddleOfRound(table, realTimeHand, actionOnSeat);
            }

            return new HandState(realTimeHand.getHandDealerState(),
                    realTimeHand.getHandRoundState());
        }
    }

    @Override
    public HandState call(Game game, Table table, User user) {
        synchronized (this) {
            RealTimeGame realTimeGame = realTimeGameBso.get(game);
            RealTimeHand realTimeHand = realTimeGame.getRealTimeHand(table);
            table = realTimeGame.getTable(table);

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
            table.setTotalPotAmount(table.getTotalPotAmount() + actionOnSeat.getCallAmount());

            actionOnSeat.setCallAmount(0);
            actionOnSeat.setRaiseTo(0);

            if (actionOnSeat.equals(realTimeHand.getLastToAct())) {
                handleEndOfRound(game, table, realTimeHand,
                        realTimeGame.getCurrentBlinds().getBigBlind());
            } else {
                handleMiddleOfRound(table, realTimeHand, actionOnSeat);
            }

            return new HandState(realTimeHand.getHandDealerState(),
                    realTimeHand.getHandRoundState());
        }
    }

    @Override
    public HandState raise(Game game, Table table, User user, String raiseAmount) {
        synchronized (this) {
            RealTimeGame realTimeGame = realTimeGameBso.get(game);
            RealTimeHand realTimeHand = realTimeGame.getRealTimeHand(table);
            table = realTimeGame.getTable(table);

            Blinds currentBlinds = realTimeGame.getCurrentBlinds();
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
            int raiseAboveCall = raiseAmountInt - actionOnSeat.getCallAmount();
            int increaseOfChipsInFront = raiseAmountInt - actionOnSeat.getChipsInFront();

            realTimeHand.setOriginatingBettor(actionOnSeat);
            determineLastToAct(table, realTimeHand);
            handleMiddleOfRound(table, realTimeHand, actionOnSeat);
            actionOnSeat.setChipsInFront(raiseAmountInt);

            UserGameStatus userGameStatus = actionOnSeat.getUserGameStatus();
            userGameStatus.setChips(userGameStatus.getChips() - increaseOfChipsInFront);
            table.setTotalPotAmount(table.getTotalPotAmount() + increaseOfChipsInFront);

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

    private void handleMiddleOfRound(Table table, RealTimeHand realTimeHand, Seat actionOnSeat) {
        realTimeHand.setHandRoundState(HandRoundState.ROUND_IN_PROGRESS);
        actionOnSeat.setActionOn(false);
        realTimeHand.getNextToAct().setActionOn(true);
        determineNextToAct(table, realTimeHand);
    }

    private void handleEndOfRound(Game game, Table table,
            RealTimeHand realTimeHand, int bigBlindAmount) {
        realTimeHand.setOriginatingBettor(null);
        realTimeHand.setHandRoundState(HandRoundState.ROUND_COMPLETE);
        moveToNextHandDealerState(realTimeHand);
        potBso.calculatePotsAfterRound(game, table);
        determineTablePotAmounts(game, table);
        resetPossibleSeatActionsAfterRound(table, realTimeHand);

        if (realTimeHand.getHandDealerState() == HandDealerState.COMPLETE) {
            seatStatusBso.setStatusForEndOfHand(table);
            determineWinners(game, table, realTimeHand.getHandEvaluationList());
        } else {
            seatStatusBso.setStatusForNewRound(table);
            determineNextToAct(table, realTimeHand);
            determineLastToAct(table, realTimeHand);
            resetRaiseAmountsAfterRound(table, bigBlindAmount);
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
            RealTimeHand realTimeHand) {
        for (Seat seat : table.getSeats()) {
            realTimeHand.addPossibleSeatAction(seat, GameEventType.CHECK);
            realTimeHand.addPossibleSeatAction(seat, GameEventType.RAISE);
            realTimeHand.removePossibleSeatAction(seat, GameEventType.CALL);
            realTimeHand.removePossibleSeatAction(seat, GameEventType.FOLD);
        }
    }

    private void resetAllSeatActions(Seat seat, RealTimeHand realTimeHand) {
        realTimeHand.removePossibleSeatAction(seat, GameEventType.CHECK);
        realTimeHand.removePossibleSeatAction(seat, GameEventType.RAISE);
        realTimeHand.removePossibleSeatAction(seat, GameEventType.CALL);
        realTimeHand.removePossibleSeatAction(seat, GameEventType.FOLD);
    }

    private void determineLastToAct(Table table, RealTimeHand realTimeHand) {
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

    private void determineNextToAct(Table table, RealTimeHand realTimeHand) {
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
            User user, RealTimeHand realTimeHand, Table table) {

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

    private void moveToNextHandDealerState(RealTimeHand realTimeHand) {
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
            default:
                throw new IllegalStateException("No valid state to move to.");
        }
    }

    private void determineTablePotAmounts(Game game, Table table) {
        table.setPotAmounts(new ArrayList<Integer>());
        for (Pot pot : potBso.fetchAllPots(game, table)) {
            table.getPotAmounts().add(pot.getAmount());
        }
    }

    private void determineWinners(Game game, Table table, List<HandEvaluation> handEvaluationList) {
        potBso.setWinners(game, table, handEvaluationList);

        for (Pot pot : potBso.fetchAllPots(game, table)) {
            List<Seat> winners = pot.getWinners();
            int numberOfWinners = winners.size();
            int numberOfChips = pot.getAmount() / numberOfWinners;
            int bonusChips = pot.getAmount() % numberOfWinners;

            winners.get(0).getUserGameStatus().setChips(
                    winners.get(0).getUserGameStatus().getChips() + bonusChips);

            for (Seat winner : winners) {
                winner.getUserGameStatus().setChips(
                        winner.getUserGameStatus().getChips() + numberOfChips);
                PocketCards pocketCards = deckBso.fetchPocketCards(
                        winner.getUserGameStatus().getUser(), game, table);
                winner.setShowCards(pocketCards);
            }
        }
    }

    public RealTimeGameBso getRealTimeGameBso() {
        return realTimeGameBso;
    }

    public void setRealTimeGameBso(RealTimeGameBso realTimeGameBso) {
        this.realTimeGameBso = realTimeGameBso;
    }

    public PotBso getPotBso() {
        return potBso;
    }

    public void setPotBso(PotBso potBso) {
        this.potBso = potBso;
    }

    public SeatStatusBso getSeatStatusBso() {
        return seatStatusBso;
    }

    public void setSeatStatusBso(SeatStatusBso seatStatusBso) {
        this.seatStatusBso = seatStatusBso;
    }

    public ValidationBso getValidationBso() {
        return validationBso;
    }

    public void setValidationBso(ValidationBso validationBso) {
        this.validationBso = validationBso;
    }

    public DeckBso getDeckBso() {
        return deckBso;
    }

    public void setDeckBso(DeckBso deckBso) {
        this.deckBso = deckBso;
    }

}
