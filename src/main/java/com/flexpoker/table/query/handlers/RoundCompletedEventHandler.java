package com.flexpoker.table.query.handlers;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.pushnotifications.TableUpdatedPushNotification;
import com.flexpoker.table.command.events.RoundCompletedEvent;
import com.flexpoker.table.query.dto.SeatDTO;
import com.flexpoker.table.query.dto.TableDTO;
import com.flexpoker.table.query.repository.TableRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.stream.Collectors;

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
        var currentTable = tableRepository.fetchById(event.getAggregateId());

        var updatedSeats = currentTable.getSeats().stream()
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

        var updatedTable = new TableDTO(currentTable.getId(),
                event.getVersion(), updatedSeats, currentTable.getTotalPot(),
                currentTable.getPots(), currentTable.getVisibleCommonCards(),
                currentTable.getCurrentHandMinRaiseToAmount(),
                currentTable.getCurrentHandId());
        tableRepository.save(updatedTable);
    }

    private void handlePushNotifications(RoundCompletedEvent event) {
        var pushNotification = new TableUpdatedPushNotification(event.getGameId(), event.getAggregateId());
        pushNotificationPublisher.publish(pushNotification);
    }

}
