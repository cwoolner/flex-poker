package com.flexpoker.table.query.handlers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.core.api.chat.SendTableChatMessageCommand;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotification;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.login.repository.LoginRepository;
import com.flexpoker.model.chat.outgoing.TableChatMessage;
import com.flexpoker.pushnotifications.TableUpdatedPushNotification;
import com.flexpoker.table.command.events.PlayerForceFoldedEvent;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.dto.outgoing.PotDTO;
import com.flexpoker.web.dto.outgoing.SeatDTO;
import com.flexpoker.web.dto.outgoing.TableDTO;

@Component
public class PlayerForceFoldedEventHandler implements EventHandler<PlayerForceFoldedEvent> {

    private final SendTableChatMessageCommand sendTableChatMessageCommand;

    private final LoginRepository loginRepository;

    private final TableRepository tableRepository;

    private final PushNotificationPublisher pushNotificationPublisher;

    @Inject
    public PlayerForceFoldedEventHandler(
            SendTableChatMessageCommand sendTableChatMessageCommand,
            LoginRepository loginRepository, TableRepository tableRepository,
            PushNotificationPublisher pushNotificationPublisher) {
        this.sendTableChatMessageCommand = sendTableChatMessageCommand;
        this.loginRepository = loginRepository;
        this.tableRepository = tableRepository;
        this.pushNotificationPublisher = pushNotificationPublisher;
    }

    @Override
    public void handle(PlayerForceFoldedEvent event) {
        handleUpdatingTable(event);
        handlePushNotifications(event);
        handleChat(event);
    }

    private void handleUpdatingTable(PlayerForceFoldedEvent event) {
        TableDTO currentTable = tableRepository.fetchById(event.getAggregateId());
        String username = loginRepository.fetchUsernameByAggregateId(event.getPlayerId());

        List<SeatDTO> updatedSeats = currentTable.getSeats().stream()
                .map(seatDTO -> {
                    if (seatDTO.getName().equals(username)) {
                        return new SeatDTO(
                                seatDTO.getPosition(),
                                seatDTO.getName(),
                                seatDTO.getChipsInBack(),
                                seatDTO.getChipsInFront(),
                                false,
                                0,
                                0,
                                seatDTO.isButton(),
                                seatDTO.isSmallBlind(),
                                seatDTO.isBigBlind(),
                                false);
                    }
                    return seatDTO;
                }).collect(Collectors.toList());

        Set<PotDTO> updatePots = currentTable.getPots().stream()
                .map(potDTO -> {
                    Set<String> updatedPotSeats = potDTO.getSeats().stream()
                            .filter(x -> !x.equals(username))
                            .collect(Collectors.toSet());
                    return new PotDTO(
                            updatedPotSeats,
                            potDTO.getAmount(),
                            potDTO.isOpen(),
                            potDTO.getWinners());
        }).collect(Collectors.toSet());

        TableDTO updatedTable = new TableDTO(currentTable.getId(),
                event.getVersion(), updatedSeats, currentTable.getTotalPot(),
                updatePots, currentTable.getVisibleCommonCards(),
                currentTable.getCurrentHandMinRaiseToAmount());
        tableRepository.save(updatedTable);
    }

    private void handlePushNotifications(PlayerForceFoldedEvent event) {
        PushNotification pushNotification = new TableUpdatedPushNotification(
                event.getGameId(), event.getAggregateId());
        pushNotificationPublisher.publish(pushNotification);
    }

    private void handleChat(PlayerForceFoldedEvent event) {
        String username = loginRepository.fetchUsernameByAggregateId(event.getPlayerId());
        String message = "Time expired - " + username + " folds";
        sendTableChatMessageCommand.execute(new TableChatMessage(message, null, true,
                event.getGameId(), event.getAggregateId()));
    }

}
