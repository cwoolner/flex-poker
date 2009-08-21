package com.flexpoker.bso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flexpoker.dao.GameEventDao;
import com.flexpoker.dao.GameEventTypeDao;
import com.flexpoker.dao.SeatDao;
import com.flexpoker.dao.TableDao;
import com.flexpoker.dao.UserGameStatusDao;
import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.Blinds;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameEvent;
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

    private UserGameStatusDao userGameStatusDao;

    private DeckBso deckBso;

    private TableDao tableDao;

    private RealTimeGameBso realTimeGameBso;

    private GameEventTypeDao gameEventTypeDao;

    private GameEventDao gameEventDao;

    private SeatDao seatDao;

    private RealTimeHandBso realTimeHandBso;

    @Override
    public void addUserToGame(User user, Game game) {
        game = gameBso.fetchById(game.getId());

        String gameStage = game.getGameStage().getName();

        if (GameStage.STARTING.equals(gameStage)
            || GameStage.IN_PROGRESS.equals(gameStage)) {
            throw new FlexPokerException("The game has already started");
        }

        if (GameStage.FINISHED.equals(gameStage)) {
            throw new FlexPokerException("The game is already finished.");
        }

        Set<UserGameStatus> userGameStatuses = game.getUserGameStatuses();
        
        for (UserGameStatus userGameStatus : userGameStatuses) {
            if (user.equals(userGameStatus.getUser())) {
                throw new FlexPokerException("You are already in this game.");
            }
        }
        
        Integer totalPlayers = game.getTotalPlayers();
        Integer currentNumberOfPlayers = game.getUserGameStatuses().size();
        
        if (totalPlayers <= currentNumberOfPlayers) {
            throw new FlexPokerException("This game is full.");
        }

        UserGameStatus userGameStatus = new UserGameStatus();
        userGameStatus.setEnterTime(new Date());
        userGameStatus.setGame(game);
        userGameStatus.setUser(user);
        userGameStatusDao.save(userGameStatus.getId(), userGameStatus);
    }

    @Override
    public boolean isGameAtMaxPlayers(Game game) {
        game = gameBso.fetchById(game.getId());
        return game.getUserGameStatuses().size() == game.getTotalPlayers();
    }

    @Override
    public void verifyRegistration(User user, Game game) {
        RealTimeGame realTimeGame = realTimeGameBso.get(game);
        realTimeGame.verifyEvent(user, "registration");
    }

    @Override
    public boolean haveAllPlayersVerifiedRegistration(Game game) {
        RealTimeGame realTimeGame = realTimeGameBso.get(game);
        return realTimeGame.isEventVerified("registration");
    }

    @Override
    public PocketCards fetchPocketCards(User user, Table table) {
        table = tableDao.findById(table.getId());
        return deckBso.fetchPocketCards(user, table);
    }

    @Override
    public void startNewHand(Table table) {
        assignSeatStates(table);
        deckBso.shuffleDeck(table);
        createNewRealTimeHand(table);
    }

    private void createNewRealTimeHand(Table table) {
        Blinds currentBlinds = realTimeGameBso.get(table.getGame()).getCurrentBlinds();
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

        int actionOnIndex = seats.indexOf(table.getActionOn());

        for (int i = actionOnIndex - 1; i >= 0; i--) {
            if (seats.get(i).getStillInHand()) {
                realTimeHand.setLastToAct(seats.get(i));
                return;
            }
        }

        for (int i = seats.size() - 1; i > actionOnIndex; i--) {
            if (seats.get(i).getStillInHand()) {
                realTimeHand.setLastToAct(seats.get(i));
                return;
            }
        }
    }

    private void determineNextToAct(Table table, RealTimeHand realTimeHand) {
        List<Seat> seats = new ArrayList<Seat>(table.getSeats());
        Collections.sort(seats);

        int actionOnIndex = seats.indexOf(table.getActionOn());

        for (int i = actionOnIndex + 1; i < seats.size(); i++) {
            if (seats.get(i).getStillInHand()) {
                realTimeHand.setNextToAct(seats.get(i));
                return;
            }
        }

        for (int i = 0; i < actionOnIndex; i++) {
            if (seats.get(i).getStillInHand()) {
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
        tableDao.save(table.getId(), table);
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

        for (Table table : game.getTables()) {
            startNewHand(table);
        }

    }

    @Override
    public boolean haveAllPlayersVerifiedGameInProgress(Game game) {
        RealTimeGame realTimeGame = realTimeGameBso.get(game);
        return realTimeGame.isEventVerified("gameInProgress");
    }

    @Override
    public void verifyGameInProgress(User user, Game game) {
        RealTimeGame realTimeGame = realTimeGameBso.get(game);
        realTimeGame.verifyEvent(user, "gameInProgress");
    }

    @Override
    public void check(User user, Table table) {
        table = tableDao.findById(table.getId());

        GameEvent checkEvent = new GameEvent();
        checkEvent.setEventTime(new Date());
        checkEvent.setGame(table.getGame());
        checkEvent.setTable(table);
        checkEvent.setUser(user);
        checkEvent.setGameEventType(gameEventTypeDao.findByName(GameEventType.CHECK));
        gameEventDao.save(checkEvent.getId(), checkEvent);
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
    public boolean isUserAllowedToPerformAction(String action, User user,
            Table table) {
        table = tableDao.findById(table.getId());

        Seat usersSeat = null;

        for (Seat seat : table.getSeats()) {
            if (seat.getUserGameStatus() != null
                && user.equals(seat.getUserGameStatus().getUser())) {
                usersSeat = seat;
                break;
            }
        }

        return realTimeHandBso.get(table).isUserAllowedToPerformAction(action, usersSeat);
    }

    @Override
    public void updateState(Table table) {
        table = tableDao.findById(table.getId());
        RealTimeHand realTimeHand = realTimeHandBso.get(table);
        GameEvent gameEvent = gameEventDao.findLatestTableEvent(table);

        if (gameEvent.getGameEventType().getName().equals(GameEventType.CHECK)) {
            updateCheckState(table, realTimeHand, gameEvent);
        }
    }

    private void updateCheckState(Table table, RealTimeHand realTimeHand,
            GameEvent gameEvent) {
        if (table.getActionOn().equals(realTimeHand.getLastToAct())) {
            realTimeHand.setRoundComplete(true);
            if (realTimeHand.isRiverDealt()) {
                realTimeHand.setHandComplete(true);
            }
        } else {
            table.setActionOn(seatDao.findById(realTimeHand.getNextToAct().getId()));
        }
    }

    public UserGameStatusDao getUserGameStatusDao() {
        return userGameStatusDao;
    }

    public void setUserGameStatusDao(UserGameStatusDao userGameStatusDao) {
        this.userGameStatusDao = userGameStatusDao;
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

    public TableDao getTableDao() {
        return tableDao;
    }

    public void setTableDao(TableDao tableDao) {
        this.tableDao = tableDao;
    }

    public RealTimeGameBso getRealTimeGameBso() {
        return realTimeGameBso;
    }

    public void setRealTimeGameBso(RealTimeGameBso realTimeGameBso) {
        this.realTimeGameBso = realTimeGameBso;
    }

    public GameEventTypeDao getGameEventTypeDao() {
        return gameEventTypeDao;
    }

    public void setGameEventTypeDao(GameEventTypeDao gameEventTypeDao) {
        this.gameEventTypeDao = gameEventTypeDao;
    }

    public GameEventDao getGameEventDao() {
        return gameEventDao;
    }

    public void setGameEventDao(GameEventDao gameEventDao) {
        this.gameEventDao = gameEventDao;
    }

    public RealTimeHandBso getRealTimeHandBso() {
        return realTimeHandBso;
    }

    public void setRealTimeHandBso(RealTimeHandBso realTimeHandBso) {
        this.realTimeHandBso = realTimeHandBso;
    }

    public SeatDao getSeatDao() {
        return seatDao;
    }

    public void setSeatDao(SeatDao seatDao) {
        this.seatDao = seatDao;
    }

}
