package com.flexpoker.game.query.handlers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.chat.repository.ChatRepository;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.game.command.events.PlayerBustedGameEvent;
import com.flexpoker.login.repository.LoginRepository;
import com.flexpoker.pushnotifications.ChatSentPushNotification;
import com.flexpoker.web.dto.outgoing.ChatMessageDTO;

@Component
public class PlayerBustedGameEventHandler implements EventHandler<PlayerBustedGameEvent> {

    private final PushNotificationPublisher pushNotificationPublisher;

    private final LoginRepository loginRepository;

    private final ChatRepository chatRepository;

    @Inject
    public PlayerBustedGameEventHandler(
            PushNotificationPublisher pushNotificationPublisher,
            LoginRepository loginRepository,
            ChatRepository chatRepository) {
        this.pushNotificationPublisher = pushNotificationPublisher;
        this.loginRepository = loginRepository;
        this.chatRepository = chatRepository;
    }

    @Async
    @Override
    public void handle(PlayerBustedGameEvent event) {
        handleChat(event);
    }

    private void handleChat(PlayerBustedGameEvent event) {
        var username = loginRepository.fetchUsernameByAggregateId(event.getPlayerId());
        var message = username + " is out";
        chatRepository.saveChatMessage(
                new ChatMessageDTO(event.getAggregateId(), null, message, null, true));
        pushNotificationPublisher.publish(
                new ChatSentPushNotification(event.getAggregateId(), null, message, null, true));
    }

}
