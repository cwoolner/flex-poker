package com.flexpoker.table.query.handlers;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotification;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.login.query.repository.LoginRepository;
import com.flexpoker.pushnotifications.SendUserPocketCardsPushNotification;
import com.flexpoker.pushnotifications.TableUpdatedPushNotification;
import com.flexpoker.table.command.events.HandDealtEvent;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.model.table.SeatViewModel;
import com.flexpoker.web.model.table.TableViewModel;

@Component
public class HandDealtEventHandler implements EventHandler<HandDealtEvent> {

    private final LoginRepository loginRepository;

    private final TableRepository tableRepository;

    private final PushNotificationPublisher pushNotificationPublisher;

    @Inject
    public HandDealtEventHandler(LoginRepository loginRepository,
            TableRepository tableRepository,
            PushNotificationPublisher pushNotificationPublisher) {
        this.loginRepository = loginRepository;
        this.tableRepository = tableRepository;
        this.pushNotificationPublisher = pushNotificationPublisher;
    }

    @Async
    @Override
    public void handle(HandDealtEvent event) {
        handleUpdatingTable(event);
        handlePushNotifications(event);
    }

    private void handleUpdatingTable(HandDealtEvent event) {
        TableViewModel currentTable = tableRepository.fetchById(event.getAggregateId());

        int totalPot = event.getChipsInFrontMap().values().stream()
                .mapToInt(x -> x.intValue()).sum();

        Function<UUID, SeatViewModel> seatMapper = (UUID playerId) -> {
            int position = event.getSeatMap().entrySet().stream()
                    .filter(x -> x.getValue().equals(playerId)).findAny().get().getKey()
                    .intValue();
            String name = loginRepository.fetchUsernameByAggregateId(playerId);
            int chipsInBack = event.getChipsInBack().get(playerId);
            int chipsInFront = event.getChipsInFrontMap().get(playerId);
            int raiseTo = event.getRaiseToAmountsMap().get(playerId);
            int callAmount = event.getCallAmountsMap().get(playerId);
            boolean button = event.getButtonOnPosition() == position;
            boolean smallBlind = event.getSmallBlindPosition() == position;
            boolean bigBlind = event.getBigBlindPosition() == position;
            boolean actionOn = event.getActionOnPosition() == position;

            return new SeatViewModel(position, name, chipsInBack, chipsInFront, true,
                    raiseTo, callAmount, button, smallBlind, bigBlind, actionOn);
        };

        List<SeatViewModel> seats = event.getPlayersStillInHand().stream()
                .map(seatMapper).collect(Collectors.toList());

        TableViewModel updatedTable = new TableViewModel(currentTable.getId(), seats,
                totalPot, Collections.emptySet(), Collections.emptyList());
        tableRepository.save(updatedTable);
    }

    private void handlePushNotifications(HandDealtEvent event) {
        PushNotification pushNotification = new TableUpdatedPushNotification(
                event.getGameId(), event.getAggregateId());
        pushNotificationPublisher.publish(pushNotification);

        Consumer<UUID> pocketCardsConsumer = (UUID playerId) -> {
            PushNotification pockCardsPushNotification = new SendUserPocketCardsPushNotification(
                    playerId, event.getPlayerToPocketCardsMap().get(playerId),
                    event.getAggregateId());
            pushNotificationPublisher.publish(pockCardsPushNotification);
        };
        event.getPlayersStillInHand().forEach(pocketCardsConsumer);
    }

}
