package com.flexpoker.bso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.flexpoker.exception.FlexPokerException;
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
import com.flexpoker.model.HandState;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.Pot;
import com.flexpoker.model.RealTimeGame;
import com.flexpoker.model.RealTimeHand;
import com.flexpoker.model.RiverCard;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.TurnCard;
import com.flexpoker.model.User;
import com.flexpoker.model.UserGameStatus;
import com.flexpoker.util.ActionOnSeatPredicate;
import com.flexpoker.util.BigBlindSeatPredicate;
import com.flexpoker.util.ButtonSeatPredicate;
import com.flexpoker.util.SmallBlindSeatPredicate;

@Service("gameEventBso")
public class GameEventBsoImpl implements GameEventBso {

    private GameBso gameBso;

    private DeckBso deckBso;

    private RealTimeGameBso realTimeGameBso;

    private SeatStatusBso seatStatusBso;

    private HandEvaluatorBso handEvaluatorBso;

    private PotBso potBso;

    @Override
    public boolean addUserToGame(User user, Game game) {
        synchronized (this) {
            checkIfUserCanJoinGame(game, user);

            UserGameStatus userGameStatus = new UserGameStatus();
            userGameStatus.setUser(user);

            realTimeGameBso.get(game).addUserGameStatus(userGameStatus);

            if (gameBso.fetchUserGameStatuses(game).size() == game.getTotalPlayers()) {
                gameBso.changeGameStage(game, GameStage.STARTING);
                return true;
            }

            return false;
        }
    }

    @Override
    public boolean verifyRegistration(User user, Game game) {
        synchronized (this) {
            RealTimeGame realTimeGame = realTimeGameBso.get(game);
            realTimeGame.verifyEvent(user, "registration");

            if (realTimeGame.isEventVerified("registration")) {
                gameBso.initializePlayersAndTables(game);
                gameBso.changeGameStage(game, GameStage.IN_PROGRESS);
                return true;
            }

            return false;
        }

    }

    @Override
    public boolean verifyGameInProgress(User user, Game game) {
        synchronized (this) {
            RealTimeGame realTimeGame = realTimeGameBso.get(game);
            realTimeGame.verifyEvent(user, "gameInProgress");

            if (realTimeGame.isEventVerified("gameInProgress")) {
                startNewGameForAllTables(game);
                return true;
            }

            return false;
        }
    }

    @Override
    public boolean verifyReadyToStartNewHand(User user, Game game, Table table) {
        synchronized (this) {
            RealTimeGame realTimeGame = realTimeGameBso.get(game);
            table = realTimeGame.getTable(table);

            realTimeGame.verifyEvent(user, table, "readyToStartNewHand");

            if (realTimeGame.isEventVerified(table, "readyToStartNewHand")) {
                startNewHand(game, table);
                return true;
            }

            return false;
        }
    }

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

            if (actionOnSeat.equals(realTimeHand.getLastToAct())) {
                realTimeHand.setHandRoundState(HandRoundState.ROUND_COMPLETE);
                moveToNextHandDealerState(realTimeHand);
                potBso.calculatePotsAfterRound(game, table);
                determineTablePotAmounts(game, table);
                resetChipsInFront(table);

                if (realTimeHand.getHandDealerState() != HandDealerState.COMPLETE) {
                    seatStatusBso.setStatusForNewRound(table);
                    determineNextToAct(table, realTimeHand);
                    determineLastToAct(table, realTimeHand);
                } else {
                    determineWinners(game, table, realTimeHand.getHandEvaluationList());
                }
            } else {
                realTimeHand.setHandRoundState(HandRoundState.ROUND_IN_PROGRESS);
                actionOnSeat.setActionOn(false);
                realTimeHand.getNextToAct().setActionOn(true);
                determineNextToAct(table, realTimeHand);
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

            for (Seat seat : table.getSeats()) {
                if (seat.getUserGameStatus() != null
                    && user.equals(seat.getUserGameStatus().getUser())) {
                    seat.setStillInHand(false);
                    break;
                }
            }

            Seat actionOnSeat = (Seat) CollectionUtils.find(table.getSeats(),
                    new ActionOnSeatPredicate());

            int numberOfPlayersLeft = 0;
            for (Seat seat : table.getSeats()) {
                if (seat.isStillInHand()) {
                    numberOfPlayersLeft++;
                }
            }

            if (numberOfPlayersLeft == 1) {
                realTimeHand.setHandRoundState(HandRoundState.ROUND_COMPLETE);
                realTimeHand.setHandDealerState(HandDealerState.COMPLETE);
                potBso.calculatePotsAfterRound(game, table);
                determineTablePotAmounts(game, table);
            } else if (actionOnSeat.equals(realTimeHand.getLastToAct())) {
                realTimeHand.setHandRoundState(HandRoundState.ROUND_COMPLETE);
                moveToNextHandDealerState(realTimeHand);
                potBso.calculatePotsAfterRound(game, table);
                determineTablePotAmounts(game, table);
                resetChipsInFront(table);

                if (realTimeHand.getHandDealerState() != HandDealerState.COMPLETE) {
                    seatStatusBso.setStatusForNewRound(table);
                    determineNextToAct(table, realTimeHand);
                    determineLastToAct(table, realTimeHand);
                } else {
                    determineWinners(game, table, realTimeHand.getHandEvaluationList());
                }
            } else {
                realTimeHand.setHandRoundState(HandRoundState.ROUND_IN_PROGRESS);
                actionOnSeat.setActionOn(false);
                realTimeHand.getNextToAct().setActionOn(true);
                determineNextToAct(table, realTimeHand);
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

            Blinds currentBlinds = realTimeGame.getCurrentBlinds();
            int bigBlind = currentBlinds.getBigBlind();

            Seat actionOnSeat = (Seat) CollectionUtils.find(table.getSeats(),
                    new ActionOnSeatPredicate());

            Seat seat = actionOnSeat;

            if (!isUserAllowedToPerformAction(GameEventType.CALL, user,
                    realTimeHand, table)) {
                throw new FlexPokerException("Not allowed to call.");
            }

            if (seat.equals(realTimeHand.getLastToAct())) {
                realTimeHand.setHandRoundState(HandRoundState.ROUND_COMPLETE);
                moveToNextHandDealerState(realTimeHand);
                potBso.calculatePotsAfterRound(game, table);
                determineTablePotAmounts(game, table);
                resetChipsInFront(table);

                if (realTimeHand.getHandDealerState() != HandDealerState.COMPLETE) {
                    seatStatusBso.setStatusForNewRound(table);
                    determineNextToAct(table, realTimeHand);
                    determineLastToAct(table, realTimeHand);
                } else {
                    determineWinners(game, table, realTimeHand.getHandEvaluationList());
                }
            } else {
                realTimeHand.setHandRoundState(HandRoundState.ROUND_IN_PROGRESS);
                seat.setActionOn(false);
                realTimeHand.getNextToAct().setActionOn(true);
                determineNextToAct(table, realTimeHand);
                seat.setChipsInFront(seat.getChipsInFront() + seat.getCallAmount());
            }

            UserGameStatus userGameStatus = seat.getUserGameStatus();
            userGameStatus.setChips(userGameStatus.getChips() - seat.getCallAmount());
            table.setTotalPotAmount(table.getTotalPotAmount() + seat.getCallAmount());

            int amountNeededToCall = 0;
            int amountNeededToRaise = bigBlind;

            realTimeHand.addPossibleSeatAction(seat, GameEventType.CHECK);
            realTimeHand.removePossibleSeatAction(seat, GameEventType.CALL);
            realTimeHand.removePossibleSeatAction(seat, GameEventType.FOLD);

            seat.setCallAmount(amountNeededToCall);
            seat.setMinBet(amountNeededToRaise);

            realTimeHand.setAmountNeededToCall(seat, amountNeededToCall);
            realTimeHand.setAmountNeededToRaise(seat, amountNeededToRaise);

            return new HandState(realTimeHand.getHandDealerState(),
                    realTimeHand.getHandRoundState());
        }
    }

    private void startNewHand(Game game, Table table) {
        seatStatusBso.setStatusForNewHand(table);
        deckBso.shuffleDeck(game, table);
        createNewRealTimeHand(game, table);
        potBso.createNewHandPot(game, table);
    }

    /**
     * Zero-out the chipsInFront field for all seats at a table.  This should
     * typically be used whenever a new round is started.
     *
     * @param table
     */
    private void resetChipsInFront(Table table) {
        for (Seat seat : table.getSeats()) {
            seat.setChipsInFront(0);
        }
    }

    private void checkIfUserCanJoinGame(Game game, User user) {
        GameStage gameStage = game.getGameStage();

        if (GameStage.STARTING.equals(gameStage)
            || GameStage.IN_PROGRESS.equals(gameStage)) {
            throw new FlexPokerException("The game has already started");
        }

        if (GameStage.FINISHED.equals(gameStage)) {
            throw new FlexPokerException("The game is already finished.");
        }

        Set<UserGameStatus> userGameStatuses = gameBso.fetchUserGameStatuses(game);

        for (UserGameStatus userGameStatus : userGameStatuses) {
            if (user.equals(userGameStatus.getUser())) {
                throw new FlexPokerException("You are already in this game.");
            }
        }
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
            UserGameStatus userGameStatus = seat.getUserGameStatus();
            int amountNeededToCall = bigBlind;
            int amountNeededToRaise = bigBlind * 2;

            if (seat.equals(bigBlindSeat)) {
                amountNeededToCall = 0;
                amountNeededToRaise = bigBlind;
                realTimeHand.addPossibleSeatAction(seat, GameEventType.CHECK);
                seat.setChipsInFront(bigBlind);
                userGameStatus.setChips(userGameStatus.getChips() - bigBlind);
            } else if (seat.equals(smallBlindSeat)) {
                amountNeededToCall = smallBlind;
                realTimeHand.addPossibleSeatAction(seat, GameEventType.CALL);
                realTimeHand.addPossibleSeatAction(seat, GameEventType.FOLD);
                seat.setChipsInFront(smallBlind);
                userGameStatus.setChips(userGameStatus.getChips() - smallBlind);
            } else {
                realTimeHand.addPossibleSeatAction(seat, GameEventType.CALL);
                realTimeHand.addPossibleSeatAction(seat, GameEventType.FOLD);
                seat.setChipsInFront(0);
            }

            table.setTotalPotAmount(table.getTotalPotAmount() + seat.getChipsInFront());

            seat.setCallAmount(amountNeededToCall);
            seat.setMinBet(amountNeededToRaise);

            realTimeHand.setAmountNeededToCall(seat, amountNeededToCall);
            realTimeHand.setAmountNeededToRaise(seat, amountNeededToRaise);
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

        Seat buttonSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new ButtonSeatPredicate());

        if (realTimeHand.getOriginatingBettor() == null) {
            int buttonIndex = seats.indexOf(buttonSeat);

            for (int i = buttonIndex; i >= 0; i--) {
                if (seats.get(i).isStillInHand()) {
                    realTimeHand.setLastToAct(seats.get(i));
                    return;
                }
            }

            for (int i = seats.size() - 1; i > buttonIndex; i--) {
                if (seats.get(i).isStillInHand()) {
                    realTimeHand.setLastToAct(seats.get(i));
                    return;
                }
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
            seatStatusBso.setStatusForNewGame(table);
            deckBso.shuffleDeck(game, table);
            createNewRealTimeHand(game, table);
            potBso.createNewHandPot(game, table);
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

    public RealTimeGameBso getRealTimeGameBso() {
        return realTimeGameBso;
    }

    public void setRealTimeGameBso(RealTimeGameBso realTimeGameBso) {
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
