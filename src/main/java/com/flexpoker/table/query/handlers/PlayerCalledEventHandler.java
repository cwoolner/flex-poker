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
import com.flexpoker.web.model.table.PotViewModel;
import com.flexpoker.web.model.table.SeatViewModel;
import com.flexpoker.web.model.table.TableViewModel;

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
        TableViewModel currentTable = tableRepository.fetchById(event.getAggregateId());
        String username = loginRepository.fetchUsernameByAggregateId(event.getPlayerId());

        SeatViewModel callingSeat = currentTable.getSeats().stream()
                .filter(x -> x.getName().equals(username)).findAny().get();
        int callingAmount = callingSeat.getCallAmount();
        int updatedTotalPot = currentTable.getTotalPot() + callingAmount;

        List<SeatViewModel> updatedSeats = new ArrayList<>();

        for (SeatViewModel seatViewModel : currentTable.getSeats()) {
            if (seatViewModel.getName().equals(username)) {
                int updatedChipsInFront = seatViewModel.getChipsInFront() + callingAmount;
                int updatedChipsInBack = seatViewModel.getChipsInBack() - callingAmount;
                updatedSeats.add(new SeatViewModel(seatViewModel.getPosition(),
                        seatViewModel.getName(), updatedChipsInBack, updatedChipsInFront,
                        seatViewModel.isStillInHand(), 0, 0, seatViewModel.isButton(),
                        seatViewModel.isSmallBlind(), seatViewModel.isBigBlind(), false));
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
