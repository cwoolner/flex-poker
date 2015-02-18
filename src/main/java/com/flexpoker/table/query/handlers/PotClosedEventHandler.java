package com.flexpoker.table.query.handlers;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotification;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.pushnotifications.TableUpdatedPushNotification;
import com.flexpoker.table.command.events.PotClosedEvent;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.model.outgoing.TableDTO;

@Component
public class PotClosedEventHandler implements EventHandler<PotClosedEvent> {

    private final TableRepository tableRepository;

    private final PushNotificationPublisher pushNotificationPublisher;

    @Inject
    public PotClosedEventHandler(TableRepository tableRepository,
            PushNotificationPublisher pushNotificationPublisher) {
        this.tableRepository = tableRepository;
        this.pushNotificationPublisher = pushNotificationPublisher;
    }

    @Override
    public void handle(PotClosedEvent event) {
        handleUpdatingTable(event);
        handlePushNotifications(event);
    }

    private void handleUpdatingTable(PotClosedEvent event) {
        TableDTO currentTable = tableRepository.fetchById(event.getAggregateId());

        TableDTO updatedTable = new TableDTO(currentTable.getId(),
                currentTable.getSeats(), currentTable.getTotalPot(),
                currentTable.getPots(), currentTable.getVisibleCommonCards());
        tableRepository.save(updatedTable);
    }

    private void handlePushNotifications(PotClosedEvent event) {
        PushNotification pushNotification = new TableUpdatedPushNotification(
                event.getGameId(), event.getAggregateId());
        pushNotificationPublisher.publish(pushNotification);
    }

}
