package com.flexpoker.table.query.handlers;

import com.flexpoker.chat.service.ChatService;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.login.repository.LoginRepository;
import com.flexpoker.pushnotifications.TableUpdatedPushNotification;
import com.flexpoker.table.command.events.PlayerCalledEvent;
import com.flexpoker.table.query.dto.SeatDTO;
import com.flexpoker.table.query.dto.TableDTO;
import com.flexpoker.table.query.repository.TableRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.stream.Collectors;

@Component
public class PlayerCalledEventHandler implements EventHandler<PlayerCalledEvent> {

    private final LoginRepository loginRepository;

    private final TableRepository tableRepository;

    private final PushNotificationPublisher pushNotificationPublisher;

    private final ChatService chatService;

    @Inject
    public PlayerCalledEventHandler(LoginRepository loginRepository,
            TableRepository tableRepository,
            PushNotificationPublisher pushNotificationPublisher,
            ChatService chatService) {
        this.loginRepository = loginRepository;
        this.tableRepository = tableRepository;
        this.pushNotificationPublisher = pushNotificationPublisher;
        this.chatService = chatService;
    }

    @Override
    public void handle(PlayerCalledEvent event) {
        handleUpdatingTable(event);
        handlePushNotifications(event);
        handleChat(event);
    }

    private void handleUpdatingTable(PlayerCalledEvent event) {
        var currentTable = tableRepository.fetchById(event.getAggregateId());
        var username = loginRepository.fetchUsernameByAggregateId(event.getPlayerId());

        var updatedSeats = currentTable.getSeats().stream()
                .map(seatDTO -> {
                    if (seatDTO.getName().equals(username)) {
                        var callingAmount = seatDTO.getCallAmount();
                        var updatedChipsInFront = seatDTO.getChipsInFront() + callingAmount;
                        var updatedChipsInBack = seatDTO.getChipsInBack() - callingAmount;
                        return new SeatDTO(seatDTO.getPosition(),
                                seatDTO.getName(), updatedChipsInBack, updatedChipsInFront,
                                seatDTO.isStillInHand(), 0, 0, seatDTO.isButton(),
                                seatDTO.isSmallBlind(), seatDTO.isBigBlind(), false);
                    }
                    return seatDTO;
                }).collect(Collectors.toList());

        var callAmount = currentTable.getSeats().stream()
                .filter(x -> x.getName().equals(username))
                .findAny()
                .get()
                .getCallAmount();

        var updatedTable = new TableDTO(currentTable.getId(),
                event.getVersion(), updatedSeats, currentTable.getTotalPot() + callAmount,
                currentTable.getPots(), currentTable.getVisibleCommonCards(),
                currentTable.getCurrentHandMinRaiseToAmount(), currentTable.getCurrentHandId());
        tableRepository.save(updatedTable);
    }

    private void handlePushNotifications(PlayerCalledEvent event) {
        var pushNotification = new TableUpdatedPushNotification(event.getGameId(), event.getAggregateId());
        pushNotificationPublisher.publish(pushNotification);
    }

    private void handleChat(PlayerCalledEvent event) {
        var username = loginRepository.fetchUsernameByAggregateId(event.getPlayerId());
        var message = username + " calls";
        chatService.saveAndPushSystemTableChatMessage(event.getGameId(), event.getAggregateId(), message);
    }

}
