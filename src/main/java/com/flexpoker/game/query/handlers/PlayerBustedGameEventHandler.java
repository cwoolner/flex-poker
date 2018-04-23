package com.flexpoker.game.query.handlers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.game.command.events.PlayerBustedGameEvent;
import com.flexpoker.login.repository.LoginRepository;
import com.flexpoker.pushnotifications.ChatSentPushNotification;

@Component
public class PlayerBustedGameEventHandler implements EventHandler<PlayerBustedGameEvent> {

    private final PushNotificationPublisher pushNotificationPublisher;

    private final LoginRepository loginRepository;

    @Inject
    public PlayerBustedGameEventHandler(PushNotificationPublisher pushNotificationPublisher,
            LoginRepository loginRepository) {
        this.pushNotificationPublisher = pushNotificationPublisher;
        this.loginRepository = loginRepository;
    }

    @Async
    @Override
    public void handle(PlayerBustedGameEvent event) {
        handleChat(event);
    }

    private void handleChat(PlayerBustedGameEvent event) {
        String username = loginRepository.fetchUsernameByAggregateId(event.getPlayerId());
        String message = username + " is out";
        pushNotificationPublisher
                .publish(new ChatSentPushNotification(event.getAggregateId(), null, message, null, true));
    }

}
