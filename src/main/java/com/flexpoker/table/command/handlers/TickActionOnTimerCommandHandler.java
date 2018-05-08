package com.flexpoker.table.command.handlers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.CommandHandler;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.pushnotifications.TickActionOnTimerPushNotification;
import com.flexpoker.table.command.commands.TickActionOnTimerCommand;

@Component
public class TickActionOnTimerCommandHandler implements CommandHandler<TickActionOnTimerCommand> {

    private final PushNotificationPublisher pushNotificationPublisher;

    @Inject
    public TickActionOnTimerCommandHandler(PushNotificationPublisher pushNotificationPublisher) {
        this.pushNotificationPublisher = pushNotificationPublisher;
    }

    @Async
    @Override
    public void handle(TickActionOnTimerCommand command) {
        var pushNotification = new TickActionOnTimerPushNotification(command.getGameId(),
                command.getTableId(), command.getNumber());
        pushNotificationPublisher.publish(pushNotification);
    }

}
