package com.flexpoker.bso;

import java.util.ArrayList;
import java.util.Collections;
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

    private RealTimeHandBso realTimeHandBso;

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
        
        Integer totalPlayers = game.getTotalPlayers();
        Integer currentNumberOfPlayers = gameBso.fetchUserGameStatuses(game).size();
        
        if (totalPlayers <= currentNumberOfPlayers) {
            throw new FlexPokerException("This game is full.");
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
        assignSeatStates(table);
        deckBso.shuffleDeck(table);
        createNewRealTimeHand(game, table);
    }

    private void createNewRealTimeHand(Game game, Table table) {
        Blinds currentBlinds = realTimeGameBso.get(game).getCurrentBlinds();
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

        realTimeHandBso.put(table, realTimeHand);
    }

    private void determineLastToAct(Table table, RealTimeHand realTimeHand) {
        List<Seat> seats = new ArrayList<Seat>(table.getSeats());
        Collections.sort(seats);

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
        List<Seat> seats = new ArrayList<Seat>(table.getSeats());
        Collections.sort(seats);

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

    private void assignSeatStates(Table table) {
        List<Seat> seats = new ArrayList<Seat>(table.getSeats());
        Collections.sort(seats);
        assignButton(table, seats);
        assignSmallBlind(table, seats);
        assignBigBlind(table, seats);
        assignActionOn(table, seats);

        for (Seat seat : seats) {
            seat.setStillInHand(true);
        }
    }

    private void assignActionOn(Table table, List<Seat> seats) {
        table.setActionOn(seats.get(1));
    }

    private void assignBigBlind(Table table, List<Seat> seats) {
        table.setBigBlind(seats.get(0));
    }

    private void assignSmallBlind(Table table, List<Seat> seats) {
        table.setSmallBlind(seats.get(1));
    }

    private void assignButton(Table table, List<Seat> seats) {
        table.setButton(seats.get(0));
//        int numberOfPlayersAtTable = table.getSeats().size();
//        int dealerPosition = new Random().nextInt(numberOfPlayersAtTable) + 1;

//        for (Seat seat : table.getSeats()) {
//            if (seat.getPosition().equals(dealerPosition)) {
//                table.setButton(seat);
//                break;
//            }
//        }
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

    @Override
    public boolean isHandComplete(Table table) {
        return realTimeHandBso.get(table).isHandComplete();
    }

    @Override
    public boolean isRoundComplete(Table table) {
        return realTimeHandBso.get(table).isRoundComplete();
    }

    @Override
    public boolean isFlopDealt(Table table) {
        return realTimeHandBso.get(table).isFlopDealt();
    }

    @Override
    public boolean isRiverDealt(Table table) {
        return realTimeHandBso.get(table).isRiverDealt();
    }

    @Override
    public boolean isTurnDealt(Table table) {
        return realTimeHandBso.get(table).isTurnDealt();
    }

    @Override
    public boolean isUserAllowedToPerformAction(GameEventType action,
            User user, Table table) {
        // TODO: TableDao work.
        // table = tableDao.findById(table.getId());

        Seat usersSeat = null;

        for (Seat seat : table.getSeats()) {
            if (seat.getUserGameStatus() != null
                && user.equals(seat.getUserGameStatus().getUser())) {
                usersSeat = seat;
                break;
            }
        }

        return realTimeHandBso.get(table).isUserAllowedToPerformAction(action,
                usersSeat);
    }

    @Override
    public void updateCheckState(Table table) {
        // TODO: TableDao work.
        // table = tableDao.findById(table.getId());
        RealTimeHand realTimeHand = realTimeHandBso.get(table);

        if (table.getActionOn().equals(realTimeHand.getLastToAct())) {
            realTimeHand.setRoundComplete(true);

            if (realTimeHand.isRiverDealt()) {
                realTimeHand.setHandComplete(true);
            }

            if (!realTimeHand.isHandComplete()) {
                determineNewRoundActionOn(table);
                determineNextToAct(table, realTimeHand);
                determineLastToAct(table, realTimeHand);
            }

        } else {
            // TODO: SeatDao work.
            // table.setActionOn(seatDao.findById(realTimeHand.getNextToAct().getId()));
        }
    }

    private void determineNewRoundActionOn(Table table) {
        List<Seat> seats = new ArrayList<Seat>(table.getSeats());
        Collections.sort(seats);

        int buttonIndex = seats.indexOf(table.getButton());

        for (int i = buttonIndex + 1; i < seats.size(); i++) {
            if (seats.get(i).isStillInHand()) {
                // TODO: SeatDao work.
                // table.setActionOn(seatDao.findById(seats.get(i).getId()));
                return;
            }
        }

        for (int i = 0; i < buttonIndex; i++) {
            if (seats.get(i).isStillInHand()) {
                // TODO: SeatDao work.
                // table.setActionOn(seatDao.findById(seats.get(i).getId()));
                return;
            }
        }

    }

    @Override
    public Map<Integer, PocketCards> fetchOptionalShowCards(Game game, Table table) {
        Map<Integer, PocketCards> returnMap = new HashMap<Integer, PocketCards>();
        returnMap.put(1, new PocketCards());
        returnMap.put(2, new PocketCards());
        return returnMap;
    }

    @Override
    public Map<Integer, PocketCards> fetchRequiredShowCards(Game game, Table table) {
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

    public RealTimeHandBso getRealTimeHandBso() {
        return realTimeHandBso;
    }

    public void setRealTimeHandBso(RealTimeHandBso realTimeHandBso) {
        this.realTimeHandBso = realTimeHandBso;
    }

    @Override
    public void setRoundComplete(Table table, boolean b) {
        RealTimeHand realTimeHand = realTimeHandBso.get(table);
        realTimeHand.setRoundComplete(b);
    }

}
