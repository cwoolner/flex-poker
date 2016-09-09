package com.flexpoker.table.query.handlers;

import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotification;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.pushnotifications.TableUpdatedPushNotification;
import com.flexpoker.table.command.events.PotAmountIncreasedEvent;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.dto.outgoing.PotDTO;
import com.flexpoker.web.dto.outgoing.TableDTO;

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

        System.out.println("increase: " + event.getAmountIncreased());

        Set<PotDTO> pots = currentTable.getPots().stream()
                .map(x -> x.isOpen()
                        ? new PotDTO(x.getSeats(), x.getAmount() + event.getAmountIncreased(), x.isOpen(), x.getWinners())
                        : new PotDTO(x.getSeats(), x.getAmount(), x.isOpen(), x.getWinners()))
                .collect(Collectors.toSet());

        TableDTO updatedTable = new TableDTO(currentTable.getId(),
                event.getVersion(), currentTable.getSeats(),
                currentTable.getTotalPot(), pots,
                currentTable.getVisibleCommonCards(),
                currentTable.getCurrentHandMinRaiseToAmount());
        tableRepository.save(updatedTable);
    }

    private void handlePushNotifications(PotAmountIncreasedEvent event) {
        PushNotification pushNotification = new TableUpdatedPushNotification(
                event.getGameId(), event.getAggregateId());
        pushNotificationPublisher.publish(pushNotification);
    }

}
