package com.flexpoker.table.query.handlers;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.pushnotifications.TableUpdatedPushNotification;
import com.flexpoker.table.command.events.WinnersDeterminedEvent;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.dto.outgoing.TableDTO;

@Component
public class WinnersDeterminedEventHandler implements
        EventHandler<WinnersDeterminedEvent> {

    private final TableRepository tableRepository;

    private final PushNotificationPublisher pushNotificationPublisher;

    @Inject
    public WinnersDeterminedEventHandler(TableRepository tableRepository,
            PushNotificationPublisher pushNotificationPublisher) {
        this.tableRepository = tableRepository;
        this.pushNotificationPublisher = pushNotificationPublisher;
    }

    @Override
    public void handle(WinnersDeterminedEvent event) {
        handleUpdatingTable(event);
        handlePushNotifications(event);
    }

    private void handleUpdatingTable(WinnersDeterminedEvent event) {
        var currentTable = tableRepository.fetchById(event.getAggregateId());

        var updatedTable = new TableDTO(currentTable.getId(),
                event.getVersion(), currentTable.getSeats(),
                currentTable.getTotalPot(), currentTable.getPots(),
                currentTable.getVisibleCommonCards(),
                currentTable.getCurrentHandMinRaiseToAmount(),
                currentTable.getCurrentHandId());
        tableRepository.save(updatedTable);
    }

    private void handlePushNotifications(WinnersDeterminedEvent event) {
        var pushNotification = new TableUpdatedPushNotification(event.getGameId(), event.getAggregateId());
        pushNotificationPublisher.publish(pushNotification);
    }

}
