package com.flexpoker.table.query.handlers;

import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.pushnotifications.TableUpdatedPushNotification;
import com.flexpoker.table.command.events.PotClosedEvent;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.dto.outgoing.PotDTO;
import com.flexpoker.web.dto.outgoing.TableDTO;

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
        var currentTable = tableRepository.fetchById(event.getAggregateId());

        var pots = currentTable.getPots().stream()
                .map(x -> x.isOpen()
                        ? new PotDTO(x.getSeats(), x.getAmount(), false, x.getWinners())
                        : new PotDTO(x.getSeats(), x.getAmount(), x.isOpen(), x.getWinners()))
                .collect(Collectors.toSet());

        var updatedTable = new TableDTO(currentTable.getId(),
                event.getVersion(), currentTable.getSeats(),
                currentTable.getTotalPot(), pots,
                currentTable.getVisibleCommonCards(),
                currentTable.getCurrentHandMinRaiseToAmount(),
                currentTable.getCurrentHandId());
        tableRepository.save(updatedTable);
    }

    private void handlePushNotifications(PotClosedEvent event) {
        var pushNotification = new TableUpdatedPushNotification(event.getGameId(), event.getAggregateId());
        pushNotificationPublisher.publish(pushNotification);
    }

}
