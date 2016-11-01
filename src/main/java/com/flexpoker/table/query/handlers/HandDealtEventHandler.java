package com.flexpoker.table.query.handlers;

import java.util.Collections;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.login.repository.LoginRepository;
import com.flexpoker.pushnotifications.SendUserPocketCardsPushNotification;
import com.flexpoker.pushnotifications.TableUpdatedPushNotification;
import com.flexpoker.table.command.events.HandDealtEvent;
import com.flexpoker.table.query.repository.CardsUsedInHandRepository;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.dto.outgoing.SeatDTO;
import com.flexpoker.web.dto.outgoing.TableDTO;

@Component
public class HandDealtEventHandler implements EventHandler<HandDealtEvent> {

    private final LoginRepository loginRepository;

    private final TableRepository tableRepository;

    private final CardsUsedInHandRepository cardsUsedInHandRepository;

    private final PushNotificationPublisher pushNotificationPublisher;

    @Inject
    public HandDealtEventHandler(LoginRepository loginRepository,
            TableRepository tableRepository,
            CardsUsedInHandRepository cardsUsedInHandRepository,
            PushNotificationPublisher pushNotificationPublisher) {
        this.loginRepository = loginRepository;
        this.tableRepository = tableRepository;
        this.cardsUsedInHandRepository = cardsUsedInHandRepository;
        this.pushNotificationPublisher = pushNotificationPublisher;
    }

    @Override
    public void handle(HandDealtEvent event) {
        handleCardsUsedInHandStorage(event);
        handleUpdatingTable(event);
        handlePushNotifications(event);
    }

    private void handleCardsUsedInHandStorage(HandDealtEvent event) {
        cardsUsedInHandRepository.saveFlopCards(event.getHandId(), event.getFlopCards());
        cardsUsedInHandRepository.saveTurnCard(event.getHandId(), event.getTurnCard());
        cardsUsedInHandRepository.saveRiverCard(event.getHandId(), event.getRiverCard());
        cardsUsedInHandRepository.savePocketCards(event.getHandId(), event.getPlayerToPocketCardsMap());
    }

    private void handleUpdatingTable(HandDealtEvent event) {
        var currentTable = tableRepository.fetchById(event.getAggregateId());

        var totalPot = event.getChipsInFrontMap().values().stream()
                .mapToInt(x -> x.intValue()).sum();

        Function<UUID, SeatDTO> seatMapper = (UUID playerId) -> {
            var position = event.getSeatMap().entrySet().stream()
                    .filter(x -> x.getValue().equals(playerId))
                    .findAny()
                    .get()
                    .getKey()
                    .intValue();
            var name = loginRepository.fetchUsernameByAggregateId(playerId);
            var chipsInBack = event.getChipsInBack().get(playerId);
            var chipsInFront = event.getChipsInFrontMap().get(playerId);
            var raiseTo = event.getRaiseToAmountsMap().get(playerId);
            var callAmount = event.getCallAmountsMap().get(playerId);
            var button = event.getButtonOnPosition() == position;
            var smallBlind = event.getSmallBlindPosition() == position;
            var bigBlind = event.getBigBlindPosition() == position;

            return new SeatDTO(position, name, chipsInBack, chipsInFront, true,
                    raiseTo, callAmount, button, smallBlind, bigBlind, false);
        };

        var seats = event.getPlayersStillInHand().stream()
                .map(seatMapper)
                .collect(Collectors.toList());

        var updatedTable = new TableDTO(currentTable.getId(),
                event.getVersion(), seats, totalPot, Collections.emptySet(),
                Collections.emptyList(), event.getBigBlind(), event.getHandId());
        tableRepository.save(updatedTable);
    }

    private void handlePushNotifications(HandDealtEvent event) {
        var pushNotification = new TableUpdatedPushNotification(event.getGameId(), event.getAggregateId());
        pushNotificationPublisher.publish(pushNotification);

        Consumer<UUID> pocketCardsConsumer = playerId -> {
            var pockCardsPushNotification = new SendUserPocketCardsPushNotification(
                    playerId, event.getHandId(), event.getPlayerToPocketCardsMap().get(playerId));
            pushNotificationPublisher.publish(pockCardsPushNotification);
        };
        event.getPlayersStillInHand().forEach(pocketCardsConsumer);
    }

}
