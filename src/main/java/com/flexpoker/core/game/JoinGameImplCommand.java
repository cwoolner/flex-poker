package com.flexpoker.core.game;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.flexpoker.config.Command;
import com.flexpoker.core.api.chat.SendGameChatMessageCommand;
import com.flexpoker.core.api.game.ChangeGameStageCommand;
import com.flexpoker.core.api.game.JoinGameCommand;
import com.flexpoker.dao.api.GameDao;
import com.flexpoker.dao.api.UserDao;
import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.OpenGameForUser;
import com.flexpoker.model.RealTimeGame;
import com.flexpoker.model.User;
import com.flexpoker.model.UserGameStatus;
import com.flexpoker.model.chat.outgoing.GameChatMessage;
import com.flexpoker.repository.api.OpenGameForUserRepository;
import com.flexpoker.repository.api.RealTimeGameRepository;
import com.flexpoker.util.MessagingConstants;
import com.flexpoker.web.model.AvailableTournamentListViewModel;
import com.flexpoker.web.translator.GameListTranslator;

@Command
public class JoinGameImplCommand implements JoinGameCommand {

    private final GameDao gameDao;
    
    private final UserDao userDao;
    
    private final RealTimeGameRepository realTimeGameRepository;
    
    private final ChangeGameStageCommand changeGameStageCommand;
    
    private final SimpMessageSendingOperations messagingTemplate;
    
    private final SendGameChatMessageCommand sendGameChatMessageCommand;
    
    private final OpenGameForUserRepository openGameForUserRepository;

    @Inject
    public JoinGameImplCommand(GameDao gameDao, UserDao userDao,
            RealTimeGameRepository realTimeGameRepository,
            ChangeGameStageCommand changeGameStageCommand,
            SimpMessageSendingOperations messageSendingOperations,
            SendGameChatMessageCommand sendGameChatMessageCommand,
            OpenGameForUserRepository openGameForUserRepository) {
        this.gameDao = gameDao;
        this.userDao = userDao;
        this.realTimeGameRepository = realTimeGameRepository;
        this.changeGameStageCommand = changeGameStageCommand;
        this.messagingTemplate = messageSendingOperations;
        this.sendGameChatMessageCommand = sendGameChatMessageCommand;
        this.openGameForUserRepository = openGameForUserRepository;
    }
    
    @Override
    public void execute(Integer gameId, Principal principal) {
        synchronized (this) {
            Game game = gameDao.findById(gameId);
            User user = userDao.findByUsername(principal.getName());
            RealTimeGame realTimeGame = realTimeGameRepository.get(game.getId());

            checkIfUserCanJoinGame(game, realTimeGame, user);

            UserGameStatus userGameStatus = new UserGameStatus();
            userGameStatus.setUser(user);

            realTimeGame.addUserGameStatus(userGameStatus);

            OpenGameForUser openGameForUser = new OpenGameForUser(gameId,
                    game.getId().toString(), GameStage.REGISTERING);
            openGameForUserRepository.addOpenGameForUser(principal.getName(), openGameForUser);
            
            if (realTimeGame.getUserGameStatuses().size() == game.getTotalPlayers()) {
                changeGameStageCommand.execute(gameId, GameStage.STARTING);
                List<AvailableTournamentListViewModel> allGames = new GameListTranslator().translate(gameDao.findAll());
                messagingTemplate.convertAndSend("/topic/availabletournaments-updates", allGames);
                
                for (UserGameStatus joinUserGameStatus : realTimeGame.getUserGameStatuses()) {
                    String username = joinUserGameStatus.getUser().getUsername();
                    openGameForUserRepository.setGameStage(username, gameId, GameStage.STARTING);
                    messagingTemplate.convertAndSendToUser(username, MessagingConstants.OPEN_GAMES_FOR_USER,
                            openGameForUserRepository.fetchAllOpenGamesForUser(username));
                }
            } else {
                messagingTemplate.convertAndSendToUser(principal.getName(),
                        MessagingConstants.OPEN_GAMES_FOR_USER,
                        openGameForUserRepository.fetchAllOpenGamesForUser(principal.getName()));
            }

            String message = principal.getName() + " has joined the game";
            sendGameChatMessageCommand.execute(new GameChatMessage(message, null, true, gameId));
        }
    }

    private void checkIfUserCanJoinGame(Game game, RealTimeGame realTimeGame, User user) {
        GameStage gameStage = game.getGameStage();

        if (GameStage.STARTING.equals(gameStage)
                || GameStage.INPROGRESS.equals(gameStage)) {
            throw new FlexPokerException("The game has already started");
        }

        if (GameStage.FINISHED.equals(gameStage)) {
            throw new FlexPokerException("The game is already finished.");
        }

        Set<UserGameStatus> userGameStatuses = realTimeGame.getUserGameStatuses();

        for (UserGameStatus userGameStatus : userGameStatuses) {
            if (user.equals(userGameStatus.getUser())) {
                throw new FlexPokerException("You are already in this game.");
            }
        }
    }

}
