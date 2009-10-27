package com.flexpoker.bso;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.Blinds;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameEventType;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.HandDealerState;
import com.flexpoker.model.HandRoundState;
import com.flexpoker.model.HandState;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.RealTimeGame;
import com.flexpoker.model.RealTimeHand;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;
import com.flexpoker.model.UserGameStatus;

@Transactional
@Service("gameEventBso")
public class GameEventBsoImpl implements GameEventBso {

    private GameBso gameBso;

    private DeckBso deckBso;

    private RealTimeGameBso realTimeGameBso;

    private SeatStatusBso seatStatusBso;

    @Override
    public boolean addUserToGame(User user, Game game) {
        synchronized (this) {
            game = gameBso.fetchById(game.getId());

            checkIfUserCanJoinGame(game, user);

            UserGameStatus userGameStatus = new UserGameStatus();
            userGameStatus.setEnterTime(new Date());
            userGameStatus.setUser(user);

            realTimeGameBso.get(game).addUserGameStatus(userGameStatus);

            if (gameBso.fetchUserGameStatuses(game).size() == game.getTotalPlayers()) {
                gameBso.changeGameStage(game, GameStage.STARTING);
                return true;
            }

            return false;
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
    public void startNewHand(Game game, Table table) {
        seatStatusBso.setStatusForNewGame(table);
        deckBso.shuffleDeck(table);
        createNewRealTimeHand(game, table);
    }

    private void createNewRealTimeHand(Game game, Table table) {
        RealTimeGame realTimeGame = realTimeGameBso.get(game);
        Blinds currentBlinds = realTimeGame.getCurrentBlinds();
        int smallBlind = currentBlinds.getSmallBlind();
        int bigBlind = currentBlinds.getBigBlind();

        RealTimeHand realTimeHand = new RealTimeHand(table.getSeats());

        for (Seat seat : table.getSeats()) {
            int amountNeededToCall = bigBlind;
            int amountNeededToRaise = bigBlind * 2;

            if (seat.equals(table.getBigBlind())) {
                amountNeededToCall = 0;
                amountNeededToRaise = bigBlind;
                realTimeHand.addPossibleSeatAction(seat, GameEventType.CHECK);
            } else if (seat.equals(table.getSmallBlind())) {
                amountNeededToCall = smallBlind;
                amountNeededToRaise = bigBlind + smallBlind;
                // TODO: This shouldn't be here, but it's being set just for
                //       testing purposes.  The small blind should not be able
                //       to check.
                realTimeHand.addPossibleSeatAction(seat, GameEventType.CHECK);
            } else {
                realTimeHand.addPossibleSeatAction(seat, GameEventType.CHECK);
            }

            realTimeHand.setAmountNeededToCall(seat, amountNeededToCall);
            realTimeHand.setAmountNeededToRaise(seat, amountNeededToRaise);
        }

        determineNextToAct(table, realTimeHand);
        determineLastToAct(table, realTimeHand);

        realTimeHand.setHandDealerState(HandDealerState.POCKET_CARDS_DEALT);
        realTimeHand.setHandRoundState(HandRoundState.ROUND_IN_PROGRESS);

        realTimeGame.addRealTimeHand(table, realTimeHand);
    }

    private void determineLastToAct(Table table, RealTimeHand realTimeHand) {
        List<Seat> seats = table.getSeats();

        if (realTimeHand.getOriginatingBettor() == null) {
            int buttonIndex = seats.indexOf(table.getButton());

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
        int actionOnIndex = seats.indexOf(table.getActionOn());

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

    @Override
    public void startNewHandForAllTables(Game game) {
        game = gameBso.fetchById(game.getId());

        List<Table> tables = gameBso.fetchTables(game);

        for (Table table : tables) {
            startNewHand(game, table);
        }

    }

    @Override
    public boolean verifyGameInProgress(User user, Game game) {
        synchronized (this) {
            RealTimeGame realTimeGame = realTimeGameBso.get(game);
            realTimeGame.verifyEvent(user, "gameInProgress");

            if (realTimeGame.isEventVerified("gameInProgress")) {
                startNewHandForAllTables(game);
                return true;
            }

            return false;
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

            if (table.getActionOn().equals(realTimeHand.getLastToAct())) {
                realTimeHand.setHandRoundState(HandRoundState.ROUND_COMPLETE);
                moveToNextHandDealerState(realTimeHand);

                if (realTimeHand.getHandDealerState() != HandDealerState.COMPLETE) {
                    seatStatusBso.setStatusForNewRound(table);
                    determineNextToAct(table, realTimeHand);
                    determineLastToAct(table, realTimeHand);
                }
            } else {
                realTimeHand.setHandRoundState(HandRoundState.ROUND_IN_PROGRESS);
                table.setActionOn(realTimeHand.getNextToAct());
            }

            return new HandState(realTimeHand.getHandDealerState(),
                    realTimeHand.getHandRoundState());
        }
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

    @Override
    public Map<Integer, PocketCards> fetchOptionalShowCards(Game game, Table table) {
        // TODO: This should have an additional "can they do this? check.
        // TODO: Dummy data
        Map<Integer, PocketCards> returnMap = new HashMap<Integer, PocketCards>();
        returnMap.put(1, new PocketCards());
        returnMap.put(2, new PocketCards());
        return returnMap;
    }

    @Override
    public Map<Integer, PocketCards> fetchRequiredShowCards(Game game, Table table) {
        // TODO: This should have an additional "can they do this? check.
        // TODO: Dummy data
        Map<Integer, PocketCards> returnMap = new HashMap<Integer, PocketCards>();
        returnMap.put(1, new PocketCards());
        returnMap.put(2, new PocketCards());
        return returnMap;
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

}
