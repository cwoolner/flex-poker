package com.flexpoker.table.query.handlers;

import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.chat.service.ChatService;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.login.repository.LoginRepository;
import com.flexpoker.pushnotifications.TableUpdatedPushNotification;
import com.flexpoker.table.command.events.PlayerFoldedEvent;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.dto.outgoing.PotDTO;
import com.flexpoker.web.dto.outgoing.SeatDTO;
import com.flexpoker.web.dto.outgoing.TableDTO;

@Component
public class PlayerFoldedEventHandler implements EventHandler<PlayerFoldedEvent> {

    private final LoginRepository loginRepository;

    private final TableRepository tableRepository;

    private final PushNotificationPublisher pushNotificationPublisher;

    private final ChatService chatService;

    @Inject
    public PlayerFoldedEventHandler(
            LoginRepository loginRepository,
            TableRepository tableRepository,
            PushNotificationPublisher pushNotificationPublisher,
            ChatService chatService) {
        this.loginRepository = loginRepository;
        this.tableRepository = tableRepository;
        this.pushNotificationPublisher = pushNotificationPublisher;
        this.chatService = chatService;
    }

    @Override
    public void handle(PlayerFoldedEvent event) {
        handleUpdatingTable(event);
        handlePushNotifications(event);
        handleChat(event);
    }

    private void handleUpdatingTable(PlayerFoldedEvent event) {
        var currentTable = tableRepository.fetchById(event.getAggregateId());
        var username = loginRepository.fetchUsernameByAggregateId(event.getPlayerId());

        var updatedSeats = currentTable.getSeats().stream()
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

        var updatePots = currentTable.getPots().stream()
                .map(potDTO -> {
                    var updatedPotSeats = potDTO.getSeats().stream()
                            .filter(x -> !x.equals(username))
                            .collect(Collectors.toSet());
                    return new PotDTO(
                            updatedPotSeats,
                            potDTO.getAmount(),
                            potDTO.isOpen(),
                            potDTO.getWinners());
        }).collect(Collectors.toSet());

        var updatedTable = new TableDTO(currentTable.getId(),
                event.getVersion(), updatedSeats, currentTable.getTotalPot(),
                updatePots, currentTable.getVisibleCommonCards(),
                currentTable.getCurrentHandMinRaiseToAmount(), currentTable.getCurrentHandId());
        tableRepository.save(updatedTable);
    }

    private void handlePushNotifications(PlayerFoldedEvent event) {
        var pushNotification = new TableUpdatedPushNotification(event.getGameId(), event.getAggregateId());
        pushNotificationPublisher.publish(pushNotification);
    }

    private void handleChat(PlayerFoldedEvent event) {
        var username = loginRepository.fetchUsernameByAggregateId(event.getPlayerId());
        var message = username + " folds";
        chatService.saveAndPushSystemTableChatMessage(event.getGameId(), event.getAggregateId(), message);
    }

}
