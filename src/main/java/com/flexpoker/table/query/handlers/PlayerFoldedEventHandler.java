package com.flexpoker.table.query.handlers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.core.api.chat.SendTableChatMessageCommand;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotification;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.login.query.repository.LoginRepository;
import com.flexpoker.model.chat.outgoing.TableChatMessage;
import com.flexpoker.pushnotifications.TableUpdatedPushNotification;
import com.flexpoker.table.command.events.PlayerFoldedEvent;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.model.outgoing.PotDTO;
import com.flexpoker.web.model.outgoing.SeatDTO;
import com.flexpoker.web.model.outgoing.TableDTO;

@Component
public class PlayerFoldedEventHandler implements EventHandler<PlayerFoldedEvent> {

    private final SendTableChatMessageCommand sendTableChatMessageCommand;

    private final LoginRepository loginRepository;

    private final TableRepository tableRepository;

    private final PushNotificationPublisher pushNotificationPublisher;

    @Inject
    public PlayerFoldedEventHandler(
            SendTableChatMessageCommand sendTableChatMessageCommand,
            LoginRepository loginRepository, TableRepository tableRepository,
            PushNotificationPublisher pushNotificationPublisher) {
        this.sendTableChatMessageCommand = sendTableChatMessageCommand;
        this.loginRepository = loginRepository;
        this.tableRepository = tableRepository;
        this.pushNotificationPublisher = pushNotificationPublisher;
    }

    @Override
    public void handle(PlayerFoldedEvent event) {
        handleUpdatingTable(event);
        handlePushNotifications(event);
        handleChat(event);
    }

    private void handleUpdatingTable(PlayerFoldedEvent event) {
        TableDTO currentTable = tableRepository.fetchById(event.getAggregateId());
        String username = loginRepository.fetchUsernameByAggregateId(event.getPlayerId());

        List<SeatDTO> updatedSeats = new ArrayList<>();

        for (SeatDTO seatDTO : currentTable.getSeats()) {
            if (seatDTO.getName().equals(username)) {
                updatedSeats.add(new SeatDTO(seatDTO.getPosition(),
                        seatDTO.getName(), seatDTO.getChipsInBack(),
                        seatDTO.getChipsInFront(), false, 0, 0, seatDTO
                                .isButton(), seatDTO.isSmallBlind(), seatDTO
                                .isBigBlind(), false));
            } else {
                updatedSeats.add(seatDTO);
            }
        }

        Set<PotDTO> updatePots = new HashSet<>();

        for (PotDTO potDTO : currentTable.getPots()) {
            Set<String> updatedPotSeats = potDTO.getSeats();
            updatedPotSeats.remove(username);
            updatePots.add(new PotDTO(updatedPotSeats, potDTO.getAmount(),
                    potDTO.isOpen(), potDTO.getWinners()));
        }

        TableDTO updatedTable = new TableDTO(currentTable.getId(),
                updatedSeats, currentTable.getTotalPot(), updatePots,
                currentTable.getVisibleCommonCards());
        tableRepository.save(updatedTable);
    }

    private void handlePushNotifications(PlayerFoldedEvent event) {
        PushNotification pushNotification = new TableUpdatedPushNotification(
                event.getGameId(), event.getAggregateId());
        pushNotificationPublisher.publish(pushNotification);
    }

    private void handleChat(PlayerFoldedEvent event) {
        String username = loginRepository.fetchUsernameByAggregateId(event.getPlayerId());
        String message = username + " folds";
        sendTableChatMessageCommand.execute(new TableChatMessage(message, null, true,
                event.getGameId(), event.getAggregateId()));
    }

}
