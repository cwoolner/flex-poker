package com.flexpoker.table.query.handlers;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotification;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.model.card.TurnCard;
import com.flexpoker.pushnotifications.TableUpdatedPushNotification;
import com.flexpoker.table.command.events.TurnCardDealtEvent;
import com.flexpoker.table.query.repository.CardsUsedInHandRepository;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.model.table.CardViewModel;
import com.flexpoker.web.model.table.TableViewModel;

@Component
public class TurnCardDealtEventHandler implements EventHandler<TurnCardDealtEvent> {

    private final TableRepository tableRepository;

    private final CardsUsedInHandRepository cardsUsedInHandRepository;

    private final PushNotificationPublisher pushNotificationPublisher;

    @Inject
    public TurnCardDealtEventHandler(TableRepository tableRepository,
            CardsUsedInHandRepository cardsUsedInHandRepository,
            PushNotificationPublisher pushNotificationPublisher) {
        this.tableRepository = tableRepository;
        this.cardsUsedInHandRepository = cardsUsedInHandRepository;
        this.pushNotificationPublisher = pushNotificationPublisher;
    }

    @Override
    public void handle(TurnCardDealtEvent event) {
        handleUpdatingTable(event);
        handlePushNotifications(event);
    }

    private void handleUpdatingTable(TurnCardDealtEvent event) {
        TableViewModel currentTable = tableRepository.fetchById(event.getAggregateId());

        TurnCard turnCard = cardsUsedInHandRepository.fetchTurnCard(event.getHandId());
        List<CardViewModel> visibleCommonCards = currentTable.getVisibleCommonCards();
        visibleCommonCards.add(new CardViewModel(turnCard.getCard().getId()));

        TableViewModel updatedTable = new TableViewModel(currentTable.getId(),
                currentTable.getSeats(), currentTable.getTotalPot(),
                currentTable.getPots(), visibleCommonCards);
        tableRepository.save(updatedTable);
    }

    private void handlePushNotifications(TurnCardDealtEvent event) {
        PushNotification pushNotification = new TableUpdatedPushNotification(
                event.getGameId(), event.getAggregateId());
        pushNotificationPublisher.publish(pushNotification);
    }

}
