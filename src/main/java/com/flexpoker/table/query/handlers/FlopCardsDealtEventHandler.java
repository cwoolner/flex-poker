package com.flexpoker.table.query.handlers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotification;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.model.card.FlopCards;
import com.flexpoker.pushnotifications.TableUpdatedPushNotification;
import com.flexpoker.table.command.events.FlopCardsDealtEvent;
import com.flexpoker.table.query.repository.CardsUsedInHandRepository;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.model.table.CardViewModel;
import com.flexpoker.web.model.table.TableViewModel;

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

    @Async
    @Override
    public void handle(FlopCardsDealtEvent event) {
        handleUpdatingTable(event);
        handlePushNotifications(event);
    }

    private void handleUpdatingTable(FlopCardsDealtEvent event) {
        TableViewModel currentTable = tableRepository.fetchById(event.getAggregateId());

        FlopCards flopCards = cardsUsedInHandRepository.fetchFlopCards(event.getHandId());
        List<CardViewModel> visibleCommonCards = new ArrayList<>();
        visibleCommonCards.add(new CardViewModel(flopCards.getCard1().getId()));
        visibleCommonCards.add(new CardViewModel(flopCards.getCard2().getId()));
        visibleCommonCards.add(new CardViewModel(flopCards.getCard3().getId()));

        TableViewModel updatedTable = new TableViewModel(currentTable.getId(),
                currentTable.getSeats(), currentTable.getTotalPot(),
                currentTable.getPots(), visibleCommonCards);
        tableRepository.save(updatedTable);
    }

    private void handlePushNotifications(FlopCardsDealtEvent event) {
        PushNotification pushNotification = new TableUpdatedPushNotification(
                event.getGameId(), event.getAggregateId());
        pushNotificationPublisher.publish(pushNotification);
    }

}
