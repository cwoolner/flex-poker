package com.flexpoker.table.query.handlers;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.core.api.chat.SendTableChatMessageCommand;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotification;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.login.query.repository.LoginRepository;
import com.flexpoker.model.chat.outgoing.TableChatMessage;
import com.flexpoker.pushnotifications.TableUpdatedPushNotification;
import com.flexpoker.table.command.events.PlayerCheckedEvent;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.dto.outgoing.SeatDTO;
import com.flexpoker.web.dto.outgoing.TableDTO;

@Component
public class PlayerCheckedEventHandler implements EventHandler<PlayerCheckedEvent> {

    private final LoginRepository loginRepository;

    private final TableRepository tableRepository;

    private final SendTableChatMessageCommand sendTableChatMessageCommand;

    private final PushNotificationPublisher pushNotificationPublisher;

    @Inject
    public PlayerCheckedEventHandler(LoginRepository loginRepository,
            TableRepository tableRepository,
            SendTableChatMessageCommand sendTableChatMessageCommand,
            PushNotificationPublisher pushNotificationPublisher) {
        this.loginRepository = loginRepository;
        this.tableRepository = tableRepository;
        this.sendTableChatMessageCommand = sendTableChatMessageCommand;
        this.pushNotificationPublisher = pushNotificationPublisher;
    }

    @Override
    public void handle(PlayerCheckedEvent event) {
        handleUpdatingTable(event);
        handlePushNotifications(event);
        handleChat(event);
    }

    private void handleUpdatingTable(PlayerCheckedEvent event) {
        TableDTO currentTable = tableRepository.fetchById(event.getAggregateId());
        String username = loginRepository.fetchUsernameByAggregateId(event.getPlayerId());

        List<SeatDTO> updatedSeats = currentTable.getSeats().stream()
                .map(seatDTO -> {
                    if (seatDTO.getName().equals(username)) {
                        return new SeatDTO(seatDTO.getPosition(),
                                seatDTO.getName(), seatDTO.getChipsInBack(),
                                seatDTO.getChipsInFront(), seatDTO.isStillInHand(),
                                0, 0, seatDTO.isButton(), seatDTO.isSmallBlind(),
                                seatDTO.isBigBlind(), false);
                    }
                    return seatDTO;
                }).collect(Collectors.toList());

        TableDTO updatedTable = new TableDTO(currentTable.getId(),
                event.getVersion(), updatedSeats, currentTable.getTotalPot(),
                currentTable.getPots(), currentTable.getVisibleCommonCards(),
                currentTable.getCurrentHandMinRaiseToAmount());
        tableRepository.save(updatedTable);
    }

    private void handlePushNotifications(PlayerCheckedEvent event) {
        PushNotification pushNotification = new TableUpdatedPushNotification(
                event.getGameId(), event.getAggregateId());
        pushNotificationPublisher.publish(pushNotification);
    }

    private void handleChat(PlayerCheckedEvent event) {
        String username = loginRepository.fetchUsernameByAggregateId(event.getPlayerId());
        String message = username + " checks";
        sendTableChatMessageCommand.execute(new TableChatMessage(message, null, true,
                event.getGameId(), event.getAggregateId()));
    }
}
