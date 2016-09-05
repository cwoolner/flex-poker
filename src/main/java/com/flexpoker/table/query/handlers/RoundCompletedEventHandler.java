package com.flexpoker.table.query.handlers;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotification;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.pushnotifications.TableUpdatedPushNotification;
import com.flexpoker.table.command.events.RoundCompletedEvent;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.dto.outgoing.SeatDTO;
import com.flexpoker.web.dto.outgoing.TableDTO;

@Component
public class RoundCompletedEventHandler implements EventHandler<RoundCompletedEvent> {

    private final TableRepository tableRepository;

    private final PushNotificationPublisher pushNotificationPublisher;

    @Inject
    public RoundCompletedEventHandler(TableRepository tableRepository,
            PushNotificationPublisher pushNotificationPublisher) {
        this.tableRepository = tableRepository;
        this.pushNotificationPublisher = pushNotificationPublisher;
    }

    @Override
    public void handle(RoundCompletedEvent event) {
        handleUpdatingTable(event);
        handlePushNotifications(event);
    }

    private void handleUpdatingTable(RoundCompletedEvent event) {
        TableDTO currentTable = tableRepository.fetchById(event.getAggregateId());

        List<SeatDTO> updatedSeats = currentTable.getSeats().stream()
                .map(seatDTO -> {
                    return new SeatDTO(
                            seatDTO.getPosition(),
                            seatDTO.getName(),
                            seatDTO.getChipsInBack(),
                            0,
                            seatDTO.isStillInHand(),
                            Math.min(seatDTO.getChipsInBack(), currentTable.getCurrentHandMinRaiseToAmount()),
                            seatDTO.getCallAmount(),
                            seatDTO.isButton(),
                            seatDTO.isSmallBlind(),
                            seatDTO.isBigBlind(),
                            seatDTO.isActionOn());
                    })
                .collect(Collectors.toList());

        TableDTO updatedTable = new TableDTO(currentTable.getId(),
                event.getVersion(), updatedSeats, currentTable.getTotalPot(),
                currentTable.getPots(), currentTable.getVisibleCommonCards(),
                currentTable.getCurrentHandMinRaiseToAmount());
        tableRepository.save(updatedTable);
    }

    private void handlePushNotifications(RoundCompletedEvent event) {
        PushNotification pushNotification = new TableUpdatedPushNotification(
                event.getGameId(), event.getAggregateId());
        pushNotificationPublisher.publish(pushNotification);
    }

}
