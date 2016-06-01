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
import com.flexpoker.table.command.events.PlayerCalledEvent;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.dto.outgoing.PotDTO;
import com.flexpoker.web.dto.outgoing.SeatDTO;
import com.flexpoker.web.dto.outgoing.TableDTO;

@Component
public class PlayerCalledEventHandler implements EventHandler<PlayerCalledEvent> {

    private final LoginRepository loginRepository;

    private final TableRepository tableRepository;

    private final SendTableChatMessageCommand sendTableChatMessageCommand;

    private final PushNotificationPublisher pushNotificationPublisher;

    @Inject
    public PlayerCalledEventHandler(LoginRepository loginRepository,
            TableRepository tableRepository,
            SendTableChatMessageCommand sendTableChatMessageCommand,
            PushNotificationPublisher pushNotificationPublisher) {
        this.loginRepository = loginRepository;
        this.tableRepository = tableRepository;
        this.sendTableChatMessageCommand = sendTableChatMessageCommand;
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

        SeatDTO callingSeat = currentTable.getSeats().stream()
                .filter(x -> x.getName().equals(username)).findAny().get();
        int callingAmount = callingSeat.getCallAmount();
        int updatedTotalPot = currentTable.getTotalPot() + callingAmount;

        List<SeatDTO> updatedSeats = new ArrayList<>();

        for (SeatDTO seatDTO : currentTable.getSeats()) {
            if (seatDTO.getName().equals(username)) {
                int updatedChipsInFront = seatDTO.getChipsInFront() + callingAmount;
                int updatedChipsInBack = seatDTO.getChipsInBack() - callingAmount;
                updatedSeats.add(new SeatDTO(seatDTO.getPosition(),
                        seatDTO.getName(), updatedChipsInBack, updatedChipsInFront,
                        seatDTO.isStillInHand(), 0, 0, seatDTO.isButton(),
                        seatDTO.isSmallBlind(), seatDTO.isBigBlind(), false));
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
                updatedSeats, updatedTotalPot, currentTable.getPots(),
                currentTable.getVisibleCommonCards());
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
        sendTableChatMessageCommand.execute(new TableChatMessage(message, null, true,
                event.getGameId(), event.getAggregateId()));
    }

}
