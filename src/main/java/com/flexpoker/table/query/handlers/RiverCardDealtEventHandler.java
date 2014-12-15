package com.flexpoker.table.query.handlers;

import java.util.List;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotification;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.model.card.RiverCard;
import com.flexpoker.pushnotifications.TableUpdatedPushNotification;
import com.flexpoker.table.command.events.RiverCardDealtEvent;
import com.flexpoker.table.query.repository.CardsUsedInHandRepository;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.model.table.CardViewModel;
import com.flexpoker.web.model.table.TableViewModel;

@Component
public class RiverCardDealtEventHandler implements EventHandler<RiverCardDealtEvent> {

    private final TableRepository tableRepository;

    private final CardsUsedInHandRepository cardsUsedInHandRepository;

    private final PushNotificationPublisher pushNotificationPublisher;

    @Inject
    public RiverCardDealtEventHandler(TableRepository tableRepository,
            CardsUsedInHandRepository cardsUsedInHandRepository,
            PushNotificationPublisher pushNotificationPublisher) {
        this.tableRepository = tableRepository;
        this.cardsUsedInHandRepository = cardsUsedInHandRepository;
        this.pushNotificationPublisher = pushNotificationPublisher;
    }

    @Async
    @Override
    public void handle(RiverCardDealtEvent event) {
        handleUpdatingTable(event);
        handlePushNotifications(event);
    }

    private void handleUpdatingTable(RiverCardDealtEvent event) {
        TableViewModel currentTable = tableRepository.fetchById(event.getAggregateId());

        RiverCard riverCard = cardsUsedInHandRepository.fetchRiverCard(event.getHandId());
        List<CardViewModel> visibleCommonCards = currentTable.getVisibleCommonCards();
        visibleCommonCards.add(new CardViewModel(riverCard.getCard().getId()));

        TableViewModel updatedTable = new TableViewModel(currentTable.getId(),
                currentTable.getSeats(), currentTable.getTotalPot(),
                currentTable.getPots(), visibleCommonCards);
        tableRepository.save(updatedTable);
    }

    private void handlePushNotifications(RiverCardDealtEvent event) {
        PushNotification pushNotification = new TableUpdatedPushNotification(
                event.getGameId(), event.getAggregateId());
        pushNotificationPublisher.publish(pushNotification);
    }

}
