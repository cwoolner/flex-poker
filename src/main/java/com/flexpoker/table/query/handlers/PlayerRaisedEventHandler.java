package com.flexpoker.table.query.handlers;

import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.chat.service.ChatService;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.login.repository.LoginRepository;
import com.flexpoker.pushnotifications.TableUpdatedPushNotification;
import com.flexpoker.table.command.events.PlayerRaisedEvent;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.dto.outgoing.SeatDTO;
import com.flexpoker.web.dto.outgoing.TableDTO;

@Component
public class PlayerRaisedEventHandler implements EventHandler<PlayerRaisedEvent> {

    private final LoginRepository loginRepository;

    private final TableRepository tableRepository;

    private final PushNotificationPublisher pushNotificationPublisher;

    private final ChatService chatService;

    @Inject
    public PlayerRaisedEventHandler(
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
    public void handle(PlayerRaisedEvent event) {
        handleUpdatingTable(event);
        handlePushNotifications(event);
        handleChat(event);
    }

    private void handleUpdatingTable(PlayerRaisedEvent event) {
        var currentTable = tableRepository.fetchById(event.getAggregateId());
        var username = loginRepository.fetchUsernameByAggregateId(event.getPlayerId());

        var raiseToAmount = event.getRaiseToAmount();

        var updatedSeats = currentTable.getSeats().stream()
                .map(seatDTO -> {
                    var amountOverChipsInFront = raiseToAmount - seatDTO.getChipsInFront();

                    if (seatDTO.getName().equals(username)) {
                        var updatedChipsInBack = seatDTO.getChipsInBack() - amountOverChipsInFront;
                        return new SeatDTO(seatDTO.getPosition(),
                                seatDTO.getName(), updatedChipsInBack, raiseToAmount,
                                seatDTO.isStillInHand(), 0, 0, seatDTO.isButton(),
                                seatDTO.isSmallBlind(), seatDTO.isBigBlind(), false);
                    }
                    return new SeatDTO(seatDTO.getPosition(), seatDTO.getName(), seatDTO.getChipsInBack(),
                            seatDTO.getChipsInFront(), seatDTO.isStillInHand(),
                            Math.min(raiseToAmount * 2, seatDTO.getChipsInFront() + seatDTO.getChipsInBack()),
                            amountOverChipsInFront, seatDTO.isButton(), seatDTO.isSmallBlind(), seatDTO.isBigBlind(),
                            seatDTO.isActionOn());
                }).collect(Collectors.toList());

        var previousChipsInFront = currentTable.getSeats().stream()
                .filter(x -> x.getName().equals(username))
                .findAny().get()
                .getChipsInFront();
        var totalPotIncrease = raiseToAmount - previousChipsInFront;

        var updatedTable = new TableDTO(currentTable.getId(),
                event.getVersion(), updatedSeats, currentTable.getTotalPot() + totalPotIncrease,
                currentTable.getPots(), currentTable.getVisibleCommonCards(),
                currentTable.getCurrentHandMinRaiseToAmount(), currentTable.getCurrentHandId());
        tableRepository.save(updatedTable);
    }

    private void handlePushNotifications(PlayerRaisedEvent event) {
        var pushNotification = new TableUpdatedPushNotification(event.getGameId(), event.getAggregateId());
        pushNotificationPublisher.publish(pushNotification);
    }

    private void handleChat(PlayerRaisedEvent event) {
        var username = loginRepository.fetchUsernameByAggregateId(event.getPlayerId());
        var message = username + " raises to " + event.getRaiseToAmount();
        chatService.saveAndPushSystemTableChatMessage(event.getGameId(), event.getAggregateId(), message);
    }

}
