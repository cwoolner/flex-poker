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
import com.flexpoker.web.model.table.PotViewModel;
import com.flexpoker.web.model.table.SeatViewModel;
import com.flexpoker.web.model.table.TableViewModel;

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
        TableViewModel currentTable = tableRepository.fetchById(event.getAggregateId());
        String username = loginRepository.fetchUsernameByAggregateId(event.getPlayerId());

        List<SeatViewModel> updatedSeats = new ArrayList<>();

        for (SeatViewModel seatViewModel : currentTable.getSeats()) {
            if (seatViewModel.getName().equals(username)) {
                updatedSeats.add(new SeatViewModel(seatViewModel.getPosition(),
                        seatViewModel.getName(), seatViewModel.getChipsInBack(),
                        seatViewModel.getChipsInFront(), false, 0, 0, seatViewModel
                                .isButton(), seatViewModel.isSmallBlind(), seatViewModel
                                .isBigBlind(), false));
            } else {
                updatedSeats.add(seatViewModel);
            }
        }

        Set<PotViewModel> updatePots = new HashSet<>();

        for (PotViewModel potViewModel : currentTable.getPots()) {
            Set<String> updatedPotSeats = potViewModel.getSeats();
            updatedPotSeats.remove(username);
            updatePots.add(new PotViewModel(updatedPotSeats, potViewModel.getAmount(),
                    potViewModel.isOpen(), potViewModel.getWinners()));
        }

        TableViewModel updatedTable = new TableViewModel(currentTable.getId(),
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
