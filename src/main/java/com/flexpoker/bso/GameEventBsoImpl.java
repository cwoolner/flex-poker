package com.flexpoker.bso;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.flexpoker.bso.api.DeckBso;
import com.flexpoker.bso.api.GameBso;
import com.flexpoker.bso.api.HandEvaluatorBso;
import com.flexpoker.bso.api.PotBso;
import com.flexpoker.bso.api.SeatStatusBso;
import com.flexpoker.model.Blinds;
import com.flexpoker.model.CommonCards;
import com.flexpoker.model.FlopCards;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameEventType;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.HandDealerState;
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.HandRanking;
import com.flexpoker.model.HandRoundState;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.Pot;
import com.flexpoker.model.RealTimeGame;
import com.flexpoker.model.RealTimeHand;
import com.flexpoker.model.RiverCard;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.TurnCard;
import com.flexpoker.model.User;
import com.flexpoker.repository.api.RealTimeGameRepository;
import com.flexpoker.util.ActionOnSeatPredicate;
import com.flexpoker.util.BigBlindSeatPredicate;
import com.flexpoker.util.ButtonSeatPredicate;
import com.flexpoker.util.SmallBlindSeatPredicate;

@Service
public class GameEventBsoImpl {

    private GameBso gameBso;

    private DeckBso deckBso;

    private RealTimeGameRepository realTimeGameBso;

    private SeatStatusBso seatStatusBso;

    private HandEvaluatorBso handEvaluatorBso;

    private PotBso potBso;

    private void readyForNewGame(Game game) {
        gameBso.initializePlayersAndTables(game);
        gameBso.changeGameStage(game.getId(), GameStage.INPROGRESS);
    }

    private void startNewHand(Game game, Table table) {
        seatStatusBso.setStatusForNewHand(game, table);
        resetTableStatus(game, table);
    }

    private void createNewRealTimeHand(Game game, Table table) {
        RealTimeGame realTimeGame = realTimeGameBso.get(game);
        Blinds currentBlinds = realTimeGame.getCurrentBlinds();
        int smallBlind = currentBlinds.getSmallBlind();
        int bigBlind = currentBlinds.getBigBlind();

        RealTimeHand realTimeHand = new RealTimeHand(table.getSeats());
        Seat smallBlindSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new SmallBlindSeatPredicate());
        Seat bigBlindSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new BigBlindSeatPredicate());


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

            seat.getUserGameStatus().setChips(
                    seat.getUserGameStatus().getChips() - seat.getChipsInFront());

            if (callAmount > seat.getUserGameStatus().getChips()) {
                seat.setCallAmount(seat.getUserGameStatus().getChips());
            } else {
                seat.setCallAmount(callAmount);
            }

            int totalChips = seat.getUserGameStatus().getChips()
                    + seat.getChipsInFront();

            if (raiseToAmount > totalChips) {
                seat.setRaiseTo(totalChips);
            } else {
                seat.setRaiseTo(raiseToAmount);
            }

            table.setTotalPotAmount(table.getTotalPotAmount()
                    + seat.getChipsInFront());

            if (seat.getRaiseTo() > 0) {
                realTimeHand.addPossibleSeatAction(seat, GameEventType.RAISE);
            }

            if (seat.getCallAmount() > 0) {
                realTimeHand.addPossibleSeatAction(seat, GameEventType.CALL);
                realTimeHand.addPossibleSeatAction(seat, GameEventType.FOLD);
            } else {
                realTimeHand.addPossibleSeatAction(seat, GameEventType.CHECK);
            }
        }

        determineNextToAct(table, realTimeHand);
        realTimeHand.setLastToAct(bigBlindSeat);

        realTimeHand.setHandDealerState(HandDealerState.POCKET_CARDS_DEALT);
        realTimeHand.setHandRoundState(HandRoundState.ROUND_IN_PROGRESS);

        List<HandEvaluation> handEvaluations = determineHandEvaluations(game, table);
        realTimeHand.setHandEvaluationList(handEvaluations);

        realTimeGame.addRealTimeHand(table, realTimeHand);
    }

    private List<HandEvaluation> determineHandEvaluations(Game game, Table table) {
        FlopCards flopCards = deckBso.fetchFlopCards(game, table);
        TurnCard turnCard = deckBso.fetchTurnCard(game, table);
        RiverCard riverCard = deckBso.fetchRiverCard(game, table);

        CommonCards commonCards = new CommonCards(flopCards, turnCard, riverCard);

        List<HandRanking> possibleHands = handEvaluatorBso.determinePossibleHands(commonCards);

        List<HandEvaluation> handEvaluations = new ArrayList<HandEvaluation>();

        for (Seat seat : table.getSeats()) {
            if (seat.getUserGameStatus() != null
                    && seat.getUserGameStatus().getUser() != null) {
                User user = seat.getUserGameStatus().getUser();
                PocketCards pocketCards = deckBso.fetchPocketCards(user, game, table);
                HandEvaluation handEvaluation = handEvaluatorBso
                        .determineHandEvaluation(commonCards, user, pocketCards,
                         possibleHands);
                handEvaluations.add(handEvaluation);
            }
        }

        return handEvaluations;
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

    private void startNewGameForAllTables(Game game) {
        for (Table table : gameBso.fetchTables(game)) {
            seatStatusBso.setStatusForNewGame(game, table);
            resetTableStatus(game, table);
        }
    }

    private void resetTableStatus(Game game, Table table) {
        table.setTotalPotAmount(0);
        deckBso.shuffleDeck(game, table);
        potBso.createNewHandPot(game, table);
        createNewRealTimeHand(game, table);
        determineTablePotAmounts(game, table);
    }

    private void determineTablePotAmounts(Game game, Table table) {
        table.setPotAmounts(new ArrayList<Integer>());
        for (Pot pot : potBso.fetchAllPots(game, table)) {
            table.getPotAmounts().add(pot.getAmount());
        }
    }

    public GameBso getGameBso() {
        return gameBso;
    }

    public void setGameBso(GameBso gameBso) {
        this.gameBso = gameBso;
    }

    public DeckBso getDeckBso() {
        return deckBso;
    }

    public void setDeckBso(DeckBso deckBso) {
        this.deckBso = deckBso;
    }

    public RealTimeGameRepository getRealTimeGameBso() {
        return realTimeGameBso;
    }

    public void setRealTimeGameBso(RealTimeGameRepository realTimeGameBso) {
        this.realTimeGameBso = realTimeGameBso;
    }

    public SeatStatusBso getSeatStatusBso() {
        return seatStatusBso;
    }

    public void setSeatStatusBso(SeatStatusBso seatStatusBso) {
        this.seatStatusBso = seatStatusBso;
    }

    public HandEvaluatorBso getHandEvaluatorBso() {
        return handEvaluatorBso;
    }

    public void setHandEvaluatorBso(HandEvaluatorBso handEvaluatorBso) {
        this.handEvaluatorBso = handEvaluatorBso;
    }

    public PotBso getPotBso() {
        return potBso;
    }

    public void setPotBso(PotBso potBso) {
        this.potBso = potBso;
    }

}
