package com.flexpoker.table.query.handlers;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotification;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.login.repository.LoginRepository;
import com.flexpoker.pushnotifications.ChatSentPushNotification;
import com.flexpoker.pushnotifications.TableUpdatedPushNotification;
import com.flexpoker.table.command.events.PlayerCalledEvent;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.dto.outgoing.SeatDTO;
import com.flexpoker.web.dto.outgoing.TableDTO;

@Component
public class PlayerCalledEventHandler implements EventHandler<PlayerCalledEvent> {

    private final LoginRepository loginRepository;

    private final TableRepository tableRepository;

    private final PushNotificationPublisher pushNotificationPublisher;

    @Inject
    public PlayerCalledEventHandler(LoginRepository loginRepository,
            TableRepository tableRepository,
            PushNotificationPublisher pushNotificationPublisher) {
        this.loginRepository = loginRepository;
        this.tableRepository = tableRepository;
        this.pushNotificationPublisher = pushNotificationPublisher;
    }

    @Override
    public void handle(PlayerCalledEvent event) {
        handleUpdatingTable(event);
        handlePushNotifications(event);
        handleChat(event);
    }

    private void handleUpdatingTable(PlayerCalledEvent event) {
        TableDTO currentTable = tableRepository.fetchById(event.getAggregateId());
        String username = loginRepository.fetchUsernameByAggregateId(event.getPlayerId());

        List<SeatDTO> updatedSeats = currentTable.getSeats().stream()
                .map(seatDTO -> {
                    if (seatDTO.getName().equals(username)) {
                        int callingAmount = seatDTO.getCallAmount();
                        int updatedChipsInFront = seatDTO.getChipsInFront() + callingAmount;
                        int updatedChipsInBack = seatDTO.getChipsInBack() - callingAmount;
                        return new SeatDTO(seatDTO.getPosition(),
                                seatDTO.getName(), updatedChipsInBack, updatedChipsInFront,
                                seatDTO.isStillInHand(), 0, 0, seatDTO.isButton(),
                                seatDTO.isSmallBlind(), seatDTO.isBigBlind(), false);
                    }
                    return seatDTO;
                }).collect(Collectors.toList());

        int callAmount = currentTable.getSeats().stream()
                .filter(x -> x.getName().equals(username))
                .findAny().get()
                .getCallAmount();

        TableDTO updatedTable = new TableDTO(currentTable.getId(),
                event.getVersion(), updatedSeats, currentTable.getTotalPot() + callAmount,
                currentTable.getPots(), currentTable.getVisibleCommonCards(),
                currentTable.getCurrentHandMinRaiseToAmount());
        tableRepository.save(updatedTable);
    }

    private void handlePushNotifications(PlayerCalledEvent event) {
        PushNotification pushNotification = new TableUpdatedPushNotification(
                event.getGameId(), event.getAggregateId());
        pushNotificationPublisher.publish(pushNotification);
    }

    private void handleChat(PlayerCalledEvent event) {
        String username = loginRepository.fetchUsernameByAggregateId(event.getPlayerId());
        String message = username + " calls";
        pushNotificationPublisher
                .publish(new ChatSentPushNotification(event.getGameId(), event.getAggregateId(), message, null, true));
    }

}
