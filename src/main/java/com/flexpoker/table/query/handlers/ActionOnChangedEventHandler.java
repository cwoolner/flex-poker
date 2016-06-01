package com.flexpoker.table.query.handlers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotification;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.login.query.repository.LoginRepository;
import com.flexpoker.pushnotifications.TableUpdatedPushNotification;
import com.flexpoker.table.command.events.ActionOnChangedEvent;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.dto.outgoing.SeatDTO;
import com.flexpoker.web.dto.outgoing.TableDTO;

@Component
public class ActionOnChangedEventHandler implements EventHandler<ActionOnChangedEvent> {

    private final LoginRepository loginRepository;

    private final TableRepository tableRepository;

    private final PushNotificationPublisher pushNotificationPublisher;

    @Inject
    public ActionOnChangedEventHandler(LoginRepository loginRepository,
            TableRepository tableRepository,
            PushNotificationPublisher pushNotificationPublisher) {
        this.loginRepository = loginRepository;
        this.tableRepository = tableRepository;
        this.pushNotificationPublisher = pushNotificationPublisher;
    }

    @Override
    public void handle(ActionOnChangedEvent event) {
        handleUpdatingTable(event);
        handlePushNotifications(event);
    }

    private void handleUpdatingTable(ActionOnChangedEvent event) {
        TableDTO currentTable = tableRepository.fetchById(event.getAggregateId());
        String username = loginRepository.fetchUsernameByAggregateId(event.getPlayerId());

        List<SeatDTO> updatedSeats = new ArrayList<>();

        for (SeatDTO seatDTO : currentTable.getSeats()) {
            updatedSeats
                    .add(new SeatDTO(seatDTO.getPosition(), seatDTO
                            .getName(), seatDTO.getChipsInBack(), seatDTO
                            .getChipsInFront(), seatDTO.isStillInHand(),
                            seatDTO.getRaiseTo(), seatDTO.getCallAmount(),
                            seatDTO.isButton(), seatDTO.isSmallBlind(),
                            seatDTO.isBigBlind(), seatDTO.getName().equals(
                                    username)));
        }

        TableDTO updatedTable = new TableDTO(currentTable.getId(),
                updatedSeats, currentTable.getTotalPot(), currentTable.getPots(),
                currentTable.getVisibleCommonCards());
        tableRepository.save(updatedTable);
    }

    private void handlePushNotifications(ActionOnChangedEvent event) {
        PushNotification pushNotification = new TableUpdatedPushNotification(
                event.getGameId(), event.getAggregateId());
        pushNotificationPublisher.publish(pushNotification);
    }

}
