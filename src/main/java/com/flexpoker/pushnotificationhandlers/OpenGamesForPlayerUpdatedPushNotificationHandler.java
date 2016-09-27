package com.flexpoker.pushnotificationhandlers;

import java.util.List;

import javax.inject.Inject;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.pushnotifier.PushNotificationHandler;
import com.flexpoker.game.query.dto.OpenGameForUser;
import com.flexpoker.game.query.repository.OpenGameForPlayerRepository;
import com.flexpoker.login.repository.LoginRepository;
import com.flexpoker.pushnotifications.OpenGamesForPlayerUpdatedPushNotification;
import com.flexpoker.util.MessagingConstants;

@Component
public class OpenGamesForPlayerUpdatedPushNotificationHandler implements
        PushNotificationHandler<OpenGamesForPlayerUpdatedPushNotification> {

    private final LoginRepository loginRepository;

    private final OpenGameForPlayerRepository openGameForUserRepository;

    private final SimpMessageSendingOperations messagingTemplate;

    @Inject
    public OpenGamesForPlayerUpdatedPushNotificationHandler(
            LoginRepository loginRepository,
            OpenGameForPlayerRepository openGameForUserRepository,
            SimpMessageSendingOperations messagingTemplate) {
        this.loginRepository = loginRepository;
        this.openGameForUserRepository = openGameForUserRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Async
    @Override
    public void handle(OpenGamesForPlayerUpdatedPushNotification pushNotification) {
        String username = loginRepository.fetchUsernameByAggregateId(pushNotification
                .getPlayerId());
        List<OpenGameForUser> allOpenGames = openGameForUserRepository
                .fetchAllOpenGamesForPlayer(pushNotification.getPlayerId());
        messagingTemplate.convertAndSendToUser(username,
                MessagingConstants.OPEN_GAMES_FOR_USER, allOpenGames);
    }

}
