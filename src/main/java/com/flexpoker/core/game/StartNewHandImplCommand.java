package com.flexpoker.core.game;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.context.ApplicationEventPublisher;

import com.flexpoker.bso.api.HandEvaluatorBso;
import com.flexpoker.config.Command;
import com.flexpoker.core.api.deck.CreateShuffledDeckCommand;
import com.flexpoker.core.api.game.StartNewHandCommand;
import com.flexpoker.model.Blinds;
import com.flexpoker.model.Game;
import com.flexpoker.model.Hand;
import com.flexpoker.model.HandDealerState;
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.HandRanking;
import com.flexpoker.model.HandRoundState;
import com.flexpoker.model.PlayerAction;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;
import com.flexpoker.model.card.Deck;
import com.flexpoker.model.card.FlopCards;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.model.card.RiverCard;
import com.flexpoker.model.card.TurnCard;

@Command
public class StartNewHandImplCommand implements StartNewHandCommand {

    private final CreateShuffledDeckCommand createShuffledDeckCommand;

    private final HandEvaluatorBso handEvaluatorBso;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Inject
    public StartNewHandImplCommand(CreateShuffledDeckCommand createShuffledDeckCommand,
            HandEvaluatorBso handEvaluatorBso,
            ApplicationEventPublisher applicationEventPublisher) {
        this.createShuffledDeckCommand = createShuffledDeckCommand;
        this.handEvaluatorBso = handEvaluatorBso;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void execute(Game game, Table table) {
        Blinds currentBlinds = game.getCurrentBlinds();
        int smallBlind = currentBlinds.getSmallBlind();
        int bigBlind = currentBlinds.getBigBlind();

        Deck deck = createShuffledDeckCommand.execute(table.getNumberOfPlayers());

        Hand realTimeHand = new Hand(table.getSeats(), deck);
        table.setCurrentHand(realTimeHand);

        Seat smallBlindSeat = table.getSmallBlindSeat();
        Seat bigBlindSeat = table.getBigBlindSeat();

        for (Seat seat : table.getSeats()) {
            if (seat.getUserGameStatus() == null) {
                continue;
            }

            int chipsInFront = 0;
            int callAmount = bigBlind;
            int raiseToAmount = bigBlind * 2;

            if (seat.equals(bigBlindSeat)) {
                chipsInFront = bigBlind;
                callAmount = 0;
            } else if (seat.equals(smallBlindSeat)) {
                chipsInFront = smallBlind;
                callAmount = smallBlind;
            }

            if (chipsInFront > seat.getUserGameStatus().getChips()) {
                seat.setChipsInFront(seat.getUserGameStatus().getChips());
            } else {
                seat.setChipsInFront(chipsInFront);
            }

            seat.getUserGameStatus().removeChips(seat.getChipsInFront());

            if (callAmount > seat.getUserGameStatus().getChips()) {
                seat.setCallAmount(seat.getUserGameStatus().getChips());
            } else {
                seat.setCallAmount(callAmount);
            }

            int totalChips = seat.getUserGameStatus().getChips() + seat.getChipsInFront();

            if (raiseToAmount > totalChips) {
                seat.setRaiseTo(totalChips);
            } else {
                seat.setRaiseTo(raiseToAmount);
            }

            table.getCurrentHand().addToTotalPot(seat.getChipsInFront());

            if (seat.getRaiseTo() > 0) {
                realTimeHand.addPossibleSeatAction(seat, PlayerAction.RAISE);
            }

            if (seat.getCallAmount() > 0) {
                realTimeHand.addPossibleSeatAction(seat, PlayerAction.CALL);
                realTimeHand.addPossibleSeatAction(seat, PlayerAction.FOLD);
            } else {
                realTimeHand.addPossibleSeatAction(seat, PlayerAction.CHECK);
            }
        }

        determineNextToAct(table, realTimeHand);
        realTimeHand.setLastToAct(bigBlindSeat);

        realTimeHand.setHandDealerState(HandDealerState.POCKET_CARDS_DEALT);
        realTimeHand.setHandRoundState(HandRoundState.ROUND_IN_PROGRESS);

        List<HandEvaluation> handEvaluations = determineHandEvaluations(game, table);
        realTimeHand.setHandEvaluationList(handEvaluations);

        // TODO: update table
        // applicationEventPublisher.publishEvent(new TableUpdatedEvent(this,
        // game.getId(), table));
    }

    private List<HandEvaluation> determineHandEvaluations(Game game, Table table) {
        FlopCards flopCards = table.getCurrentHand().getDeck().getFlopCards();
        TurnCard turnCard = table.getCurrentHand().getDeck().getTurnCard();
        RiverCard riverCard = table.getCurrentHand().getDeck().getRiverCard();

        List<HandRanking> possibleHands = handEvaluatorBso.determinePossibleHands(
                flopCards, turnCard, riverCard);

        List<HandEvaluation> handEvaluations = new ArrayList<>();

        for (Seat seat : table.getSeats()) {
            if (seat.getUserGameStatus() != null
                    && seat.getUserGameStatus().getUser() != null) {
                User user = seat.getUserGameStatus().getUser();
                PocketCards pocketCards = table.getCurrentHand().getDeck()
                        .getPocketCards(seat.getPosition());
                HandEvaluation handEvaluation = handEvaluatorBso.determineHandEvaluation(
                        flopCards, turnCard, riverCard, user, pocketCards, possibleHands);
                handEvaluations.add(handEvaluation);
                // TODO: send pocket cards
                // applicationEventPublisher.publishEvent(new
                // SendUserPocketCardsEvent(this,
                // user.getUsername(), pocketCards, table.getId()));
            }
        }

        return handEvaluations;
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

}
