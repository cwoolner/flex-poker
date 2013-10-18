package com.flexpoker.core.handaction;

import java.util.List;
import java.util.Set;

import com.flexpoker.core.api.chat.SendTableChatMessageCommand;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForEndOfHandCommand;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForNewRoundCommand;
import com.flexpoker.core.pot.CalculatePotsAfterRoundImplQuery;
import com.flexpoker.core.pot.DeterminePotWinnersImplQuery;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameEventType;
import com.flexpoker.model.Hand;
import com.flexpoker.model.HandDealerState;
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.HandRoundState;
import com.flexpoker.model.Pot;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.repository.api.GameRepository;

public abstract class BaseHandActionCommand {

    protected GameRepository gameRepository;
    
    protected SendTableChatMessageCommand sendTableChatMessageCommand;

    protected SetSeatStatusForEndOfHandCommand setSeatStatusForEndOfHandCommand;
    
    protected SetSeatStatusForNewRoundCommand setSeatStatusForNewRoundCommand;
    
    protected CalculatePotsAfterRoundImplQuery calculatePotsAfterRoundImplQuery;
    
    protected DeterminePotWinnersImplQuery determinePotWinnersImplQuery;

    protected void handleMiddleOfRound(Game game, Table table,
            Hand realTimeHand, Seat actionOnSeat) {
        realTimeHand.setHandRoundState(HandRoundState.ROUND_IN_PROGRESS);
        actionOnSeat.setActionOn(false);
        // TODO: stop actionOn timer
        Seat nextToActSeat = realTimeHand.getNextToAct();
        nextToActSeat.setActionOn(true);
        // TODO: start actionOn timer
        determineNextToAct(table, realTimeHand);
    }

    protected void handleEndOfRound(Game game, Table table,
            Hand realTimeHand, int bigBlindAmount) {
        realTimeHand.setOriginatingBettor(null);
        realTimeHand.setHandRoundState(HandRoundState.ROUND_COMPLETE);
        realTimeHand.moveToNextDealerState();
        Set<Pot> pots = calculatePotsAfterRoundImplQuery.execute(table);
        table.getCurrentHand().setPots(pots);

        if (realTimeHand.getHandDealerState() == HandDealerState.COMPLETE) {
            setSeatStatusForEndOfHandCommand.execute(table);
            determineWinners(table, realTimeHand.getHandEvaluationList());
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

    private void determineNextToAct(Table table, Hand realTimeHand) {
        List<Seat> seats = table.getSeats();
        Seat actionOnSeat = table.getActionOnSeat();

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

    private void determineWinners(Table table, List<HandEvaluation> handEvaluationList) {
        for (Pot pot : table.getCurrentHand().getPots()) {
            Set<Seat> winners = determinePotWinnersImplQuery.execute(table,
                    pot.getSeats(), handEvaluationList);
            pot.addWinners(winners);

            int numberOfWinners = winners.size();
            int numberOfChips = pot.getAmount() / numberOfWinners;
            int bonusChips = pot.getAmount() % numberOfWinners;
            int numberOfPlayersInPot = pot.getSeats().size();

            if (bonusChips > 0) {
                Seat seatToGiveBonusChips = ((Seat) winners.toArray()[0]);
                seatToGiveBonusChips.getUserGameStatus().setChips(
                        seatToGiveBonusChips.getUserGameStatus().getChips() + bonusChips);
            }

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

    protected void determineLastToAct(Table table, Hand realTimeHand) {
        List<Seat> seats = table.getSeats();

        int seatIndex;

        if (realTimeHand.getOriginatingBettor() == null) {
            Seat buttonSeat = table.getButtonSeat();
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

}
