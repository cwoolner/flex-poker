package com.flexpoker.game.query.handlers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.chat.service.ChatService;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.game.command.events.PlayerBustedGameEvent;
import com.flexpoker.login.repository.LoginRepository;

@Component
public class PlayerBustedGameEventHandler implements EventHandler<PlayerBustedGameEvent> {

    private final PushNotificationPublisher pushNotificationPublisher;

    private final LoginRepository loginRepository;

    private final ChatService chatService;

    @Inject
    public PlayerBustedGameEventHandler(
            PushNotificationPublisher pushNotificationPublisher,
            LoginRepository loginRepository,
            ChatService chatService) {
        this.pushNotificationPublisher = pushNotificationPublisher;
        this.loginRepository = loginRepository;
        this.chatService = chatService;
    }

    @Async
    @Override
    public void handle(PlayerBustedGameEvent event) {
        handleChat(event);
    }

    private void handleChat(PlayerBustedGameEvent event) {
        var username = loginRepository.fetchUsernameByAggregateId(event.getPlayerId());
        var message = username + " is out";
        chatService.saveAndPushSystemGameChatMessage(event.getAggregateId(), message);
    }

}
