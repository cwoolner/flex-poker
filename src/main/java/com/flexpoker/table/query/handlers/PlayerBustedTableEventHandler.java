package com.flexpoker.table.query.handlers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.login.repository.LoginRepository;
import com.flexpoker.pushnotifications.ChatSentPushNotification;
import com.flexpoker.table.command.events.PlayerBustedTableEvent;

@Component
public class PlayerBustedTableEventHandler implements EventHandler<PlayerBustedTableEvent> {

    private final PushNotificationPublisher pushNotificationPublisher;

    private final LoginRepository loginRepository;

    @Inject
    public PlayerBustedTableEventHandler(PushNotificationPublisher pushNotificationPublisher,
            LoginRepository loginRepository) {
        this.pushNotificationPublisher = pushNotificationPublisher;
        this.loginRepository = loginRepository;
    }

    @Async
    @Override
    public void handle(PlayerBustedTableEvent event) {
        handleChat(event);
    }

    private void handleChat(PlayerBustedTableEvent event) {
        var username = loginRepository.fetchUsernameByAggregateId(event.getPlayerId());
        var message = username + " is out";
        pushNotificationPublisher
                .publish(new ChatSentPushNotification(event.getGameId(), event.getAggregateId(), message, null, true));
    }

}
