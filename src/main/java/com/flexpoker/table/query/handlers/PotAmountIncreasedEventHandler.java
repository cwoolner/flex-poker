package com.flexpoker.table.query.handlers;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotification;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.pushnotifications.TableUpdatedPushNotification;
import com.flexpoker.table.command.events.PotAmountIncreasedEvent;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.model.outgoing.TableDTO;

@Component
public class PotAmountIncreasedEventHandler implements
        EventHandler<PotAmountIncreasedEvent> {

    private final TableRepository tableRepository;

    private final PushNotificationPublisher pushNotificationPublisher;

    @Inject
    public PotAmountIncreasedEventHandler(TableRepository tableRepository,
            PushNotificationPublisher pushNotificationPublisher) {
        this.tableRepository = tableRepository;
        this.pushNotificationPublisher = pushNotificationPublisher;
    }

    @Override
    public void handle(PotAmountIncreasedEvent event) {
        handleUpdatingTable(event);
        handlePushNotifications(event);
    }

    private void handleUpdatingTable(PotAmountIncreasedEvent event) {
        TableDTO currentTable = tableRepository.fetchById(event.getAggregateId());

        TableDTO updatedTable = new TableDTO(currentTable.getId(),
                currentTable.getSeats(), currentTable.getTotalPot(),
                currentTable.getPots(), currentTable.getVisibleCommonCards());
        tableRepository.save(updatedTable);
    }

    private void handlePushNotifications(PotAmountIncreasedEvent event) {
        PushNotification pushNotification = new TableUpdatedPushNotification(
                event.getGameId(), event.getAggregateId());
        pushNotificationPublisher.publish(pushNotification);
    }

}
