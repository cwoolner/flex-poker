package com.flexpoker.table.query.handlers;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotification;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.game.query.repository.OpenGameForPlayerRepository;
import com.flexpoker.login.repository.LoginRepository;
import com.flexpoker.pushnotifications.OpenGamesForPlayerUpdatedPushNotification;
import com.flexpoker.pushnotifications.OpenTableForUserPushNotification;
import com.flexpoker.table.command.events.TableCreatedEvent;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.dto.outgoing.SeatDTO;
import com.flexpoker.web.dto.outgoing.TableDTO;

@Component
public class TableCreatedEventHandler implements EventHandler<TableCreatedEvent> {

    private final LoginRepository loginRepository;

    private final TableRepository tableRepository;

    private final OpenGameForPlayerRepository openGameForPlayerRepository;

    private final PushNotificationPublisher pushNotificationPublisher;

    @Inject
    public TableCreatedEventHandler(LoginRepository loginRepository,
            TableRepository tableRepository,
            OpenGameForPlayerRepository openGameForPlayerRepository,
            PushNotificationPublisher pushNotificationPublisher) {
        this.loginRepository = loginRepository;
        this.tableRepository = tableRepository;
        this.openGameForPlayerRepository = openGameForPlayerRepository;
        this.pushNotificationPublisher = pushNotificationPublisher;
    }

    @Override
    public void handle(TableCreatedEvent event) {
        handleNewTableInsert(event);
        handleOpenGameUpdate(event);
        handlePushNotifications(event);
    }

    private void handleNewTableInsert(TableCreatedEvent event) {
        List<SeatDTO> seats = event.getSeatPositionToPlayerMap().keySet().stream()
                .map(position -> {
                    String displayName = loginRepository.fetchUsernameByAggregateId(event
                            .getSeatPositionToPlayerMap().get(Integer.valueOf(position)));
                    return SeatDTO.createForNewTable(
                            position,
                            displayName,
                            event.getStartingNumberOfChips());
                }).collect(Collectors.toList());

        TableDTO tableDTO = new TableDTO(event.getAggregateId(),
                event.getVersion(), seats, 0, Collections.emptySet(),
                Collections.emptyList(), 0);
        tableRepository.save(tableDTO);
    }

    private void handleOpenGameUpdate(TableCreatedEvent event) {
        event.getSeatPositionToPlayerMap().values().forEach(
                x -> openGameForPlayerRepository.assignTableToOpenGame(x, event.getGameId(), event.getAggregateId()));
    }

    private void handlePushNotifications(TableCreatedEvent event) {
        Consumer<UUID> openTableConsumer = (UUID playerId) -> {
            PushNotification pushNotification = new OpenTableForUserPushNotification(
                    event.getGameId(), event.getAggregateId(), playerId);
            pushNotificationPublisher.publish(pushNotification);
        };
        event.getSeatPositionToPlayerMap().values().forEach(openTableConsumer);
        event.getSeatPositionToPlayerMap().values().forEach(x -> pushNotificationPublisher
                .publish(new OpenGamesForPlayerUpdatedPushNotification(x)));
    }
}
