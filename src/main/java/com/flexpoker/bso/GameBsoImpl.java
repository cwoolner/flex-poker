package com.flexpoker.bso;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flexpoker.bso.api.GameBso;
import com.flexpoker.bso.api.TableBalancerBso;
import com.flexpoker.core.api.game.ChangeGameStageCommand;
import com.flexpoker.core.api.game.JoinGameCommand;
import com.flexpoker.dao.api.GameDao;
import com.flexpoker.dao.api.UserDao;
import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.RealTimeGame;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;
import com.flexpoker.model.UserGameStatus;
import com.flexpoker.repository.api.RealTimeGameRepository;
import com.flexpoker.web.model.AvailableTournamentListViewModel;
import com.flexpoker.web.translator.GameListTranslator;

@Transactional
@Service
public class GameBsoImpl implements GameBso {

    private final UserDao userDao;
    
    private final GameDao gameDao;

    private final RealTimeGameRepository realTimeGameBso;

    private final TableBalancerBso tableBalancerBso;

    private final SimpMessageSendingOperations messagingTemplate;
    
    private final JoinGameCommand joinGameCommand;
    
    private final ChangeGameStageCommand changeGameStageCommand;
    
    @Autowired
    public GameBsoImpl(UserDao userDao, GameDao gameDao, RealTimeGameRepository realTimeGameBso,
            TableBalancerBso tableBalancerBso, SimpMessageSendingOperations messagingTemplate,
            JoinGameCommand joinGameCommand, ChangeGameStageCommand changeGameStageCommand) {
        this.userDao = userDao;
        this.gameDao = gameDao;
        this.realTimeGameBso = realTimeGameBso;
        this.tableBalancerBso = tableBalancerBso;
        this.messagingTemplate = messagingTemplate;
        this.joinGameCommand = joinGameCommand;
        this.changeGameStageCommand = changeGameStageCommand;
    }
    
    @Override
    public Game fetchGame(Game game) {
        return gameDao.findById(game.getId());
    }

    @Override
    public List<Game> fetchAllGames() {
        return gameDao.findAll();
    }

    @Override
    public void createGame(Principal principal, Game game) {
        User user = userDao.findByUsername(principal.getName());

        game.setCreatedByUser(user);
        game.setCreatedOn(new Date());
        game.setGameStage(GameStage.REGISTERING);
        game.setAllowRebuys(false);
        gameDao.save(game);

        createRealTimeGame(game);
        
        List<AvailableTournamentListViewModel> allGames = new GameListTranslator().translate(fetchAllGames());
        
        messagingTemplate.convertAndSend("/topic/availabletournaments-updates", allGames);
    }

    @Override
    public void changeGameStage(Integer gameId, GameStage gameStage) {
        changeGameStageCommand.execute(gameId, gameStage);
    }

    @Override
    public Table fetchTable(Game game, Table table) {
        List<Table> tables = fetchTables(game);
        table = realTimeGameBso.get(game.getId()).getTable(table);

        if (table == null) {
            throw new FlexPokerException("Table does not exist.");
        }

        return table;
    }

    @Override
    public Table fetchPlayersCurrentTable(User user, Game game) {
        List<Table> tables = fetchTables(game);

        for (Table table : tables) {
            for (Seat seat : table.getSeats()) {
                if (seat.getUserGameStatus().getUser().equals(user)) {
                    return table;
                }
            }
        }

        throw new FlexPokerException("Player is not at any table.");
    }

    @Override
    public void initializePlayersAndTables(Game game) {
        RealTimeGame realTimeGame = realTimeGameBso.get(game.getId());

        List<Table> tables = tableBalancerBso.assignInitialTablesForNewGame(
                realTimeGame.getUserGameStatuses(), game.getMaxPlayersPerTable());

        for (Table table : tables) {
            realTimeGame.addTable(table);
        }

        Set<UserGameStatus> userGameStatuses = realTimeGame.getUserGameStatuses();

        for (UserGameStatus userGameStatus : userGameStatuses) {
            userGameStatus.setChips(1500);
        }
    }

    private void createRealTimeGame(Game game) {
        game = gameDao.findById(game.getId());
        realTimeGameBso.put(game.getId(), new RealTimeGame());
    }

    @Override
    public List<Table> fetchTables(Game game) {
        return realTimeGameBso.get(game.getId()).getTables();
    }

    @Override
    public Set<UserGameStatus> fetchUserGameStatuses(Game game) {
        return realTimeGameBso.get(game.getId()).getUserGameStatuses();
    }

    @Override
    public void joinGame(Integer gameId, Principal user) {
        joinGameCommand.execute(gameId, user);
    }

}
