package com.flexpoker.pushnotificationhandlers;

import java.util.List;

import javax.inject.Inject;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.pushnotifier.PushNotificationHandler;
import com.flexpoker.game.query.repository.GameListRepository;
import com.flexpoker.pushnotifications.GameListUpdatedPushNotification;
import com.flexpoker.util.MessagingConstants;
import com.flexpoker.web.model.GameInListDTO;

@Component
public class GameListUpdatedPushNotificationHandler implements
        PushNotificationHandler<GameListUpdatedPushNotification> {

    private final GameListRepository gameListRepository;

    private final SimpMessageSendingOperations messagingTemplate;

    @Inject
    public GameListUpdatedPushNotificationHandler(GameListRepository gameListRepository,
            SimpMessageSendingOperations messagingTemplate) {
        this.gameListRepository = gameListRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Async
    @Override
    public void handle(GameListUpdatedPushNotification pushNotification) {
        List<GameInListDTO> allGames = gameListRepository.fetchAll();
        messagingTemplate.convertAndSend(MessagingConstants.GAMES_UPDATED, allGames);
    }

}
