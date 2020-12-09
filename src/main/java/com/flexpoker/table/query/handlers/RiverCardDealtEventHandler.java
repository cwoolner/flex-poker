package com.flexpoker.table.query.handlers;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.pushnotifications.TableUpdatedPushNotification;
import com.flexpoker.table.command.events.RiverCardDealtEvent;
import com.flexpoker.table.query.dto.CardDTO;
import com.flexpoker.table.query.dto.TableDTO;
import com.flexpoker.table.query.repository.CardsUsedInHandRepository;
import com.flexpoker.table.query.repository.TableRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;

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

    @Override
    public void handle(RiverCardDealtEvent event) {
        handleUpdatingTable(event);
        handlePushNotifications(event);
    }

    private void handleUpdatingTable(RiverCardDealtEvent event) {
        var currentTable = tableRepository.fetchById(event.getAggregateId());

        var riverCard = cardsUsedInHandRepository.fetchRiverCard(event.getHandId());
        var visibleCommonCards = new ArrayList<>(currentTable.getVisibleCommonCards());
        visibleCommonCards.add(new CardDTO(riverCard.getCard().getId()));

        var updatedTable = new TableDTO(currentTable.getId(),
                event.getVersion(), currentTable.getSeats(),
                currentTable.getTotalPot(), currentTable.getPots(),
                visibleCommonCards, currentTable.getCurrentHandMinRaiseToAmount(),
                currentTable.getCurrentHandId());
        tableRepository.save(updatedTable);
    }

    private void handlePushNotifications(RiverCardDealtEvent event) {
        var pushNotification = new TableUpdatedPushNotification(event.getGameId(), event.getAggregateId());
        pushNotificationPublisher.publish(pushNotification);
    }

}
