package com.flexpoker.table.query.handlers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotification;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.login.query.repository.LoginRepository;
import com.flexpoker.pushnotifications.TableUpdatedPushNotification;
import com.flexpoker.table.command.events.PotCreatedEvent;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.dto.outgoing.PotDTO;
import com.flexpoker.web.dto.outgoing.TableDTO;

@Component
public class PotCreatedEventHandler implements EventHandler<PotCreatedEvent> {

    private final TableRepository tableRepository;

    private final PushNotificationPublisher pushNotificationPublisher;

    private final LoginRepository loginRepository;

    @Inject
    public PotCreatedEventHandler(TableRepository tableRepository,
            PushNotificationPublisher pushNotificationPublisher,
            LoginRepository loginRepository) {
        this.tableRepository = tableRepository;
        this.pushNotificationPublisher = pushNotificationPublisher;
        this.loginRepository = loginRepository;
    }

    @Override
    public void handle(PotCreatedEvent event) {
        handleUpdatingTable(event);
        handlePushNotifications(event);
    }

    private void handleUpdatingTable(PotCreatedEvent event) {
        TableDTO currentTable = tableRepository.fetchById(event.getAggregateId());

        Set<String> playerUsernames = event.getPlayersInvolved().stream()
                .map(x -> loginRepository.fetchUsernameByAggregateId(x))
                .collect(Collectors.toSet());

        Set<PotDTO> pots = new HashSet<>(currentTable.getPots());
        pots.add(new PotDTO(playerUsernames, 0, true, Collections.emptySet()));

        TableDTO updatedTable = new TableDTO(currentTable.getId(),
                event.getVersion(), currentTable.getSeats(),
                currentTable.getTotalPot(), pots,
                currentTable.getVisibleCommonCards(),
                currentTable.getCurrentHandMinRaiseToAmount());
        tableRepository.save(updatedTable);
    }

    private void handlePushNotifications(PotCreatedEvent event) {
        PushNotification pushNotification = new TableUpdatedPushNotification(
                event.getGameId(), event.getAggregateId());
        pushNotificationPublisher.publish(pushNotification);
    }

}
