package com.flexpoker.table.query.handlers;

import java.util.Arrays;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.pushnotifications.TableUpdatedPushNotification;
import com.flexpoker.table.command.events.FlopCardsDealtEvent;
import com.flexpoker.table.query.repository.CardsUsedInHandRepository;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.dto.outgoing.CardDTO;
import com.flexpoker.web.dto.outgoing.TableDTO;

@Component
public class FlopCardsDealtEventHandler implements EventHandler<FlopCardsDealtEvent> {

    private final TableRepository tableRepository;

    private final CardsUsedInHandRepository cardsUsedInHandRepository;

    private final PushNotificationPublisher pushNotificationPublisher;

    @Inject
    public FlopCardsDealtEventHandler(TableRepository tableRepository,
            CardsUsedInHandRepository cardsUsedInHandRepository,
            PushNotificationPublisher pushNotificationPublisher) {
        this.tableRepository = tableRepository;
        this.cardsUsedInHandRepository = cardsUsedInHandRepository;
        this.pushNotificationPublisher = pushNotificationPublisher;
    }

    @Override
    public void handle(FlopCardsDealtEvent event) {
        handleUpdatingTable(event);
        handlePushNotifications(event);
    }

    private void handleUpdatingTable(FlopCardsDealtEvent event) {
        var currentTable = tableRepository.fetchById(event.getAggregateId());

        var flopCards = cardsUsedInHandRepository.fetchFlopCards(event.getHandId());
        var visibleCommonCards = Arrays.asList(
                new CardDTO(flopCards.getCard1().getId()),
                new CardDTO(flopCards.getCard2().getId()),
                new CardDTO(flopCards.getCard3().getId()));

        var updatedTable = new TableDTO(currentTable.getId(),
                event.getVersion(), currentTable.getSeats(),
                currentTable.getTotalPot(), currentTable.getPots(),
                visibleCommonCards, currentTable.getCurrentHandMinRaiseToAmount(),
                currentTable.getCurrentHandId());
        tableRepository.save(updatedTable);
    }

    private void handlePushNotifications(FlopCardsDealtEvent event) {
        var pushNotification = new TableUpdatedPushNotification(event.getGameId(), event.getAggregateId());
        pushNotificationPublisher.publish(pushNotification);
    }

}
