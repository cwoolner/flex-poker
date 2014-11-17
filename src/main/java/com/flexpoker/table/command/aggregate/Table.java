package com.flexpoker.table.command.aggregate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.framework.domain.AggregateRoot;
import com.flexpoker.table.command.events.TableCreatedEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class Table extends AggregateRoot<TableEvent> {

    private final UUID aggregateId;

    private int aggregateVersion;

    private final UUID gameId;

    private final Map<Integer, UUID> seatMap;

    protected Table(UUID aggregateId, UUID gameId, Map<Integer, UUID> seatMap) {
        this.aggregateId = aggregateId;
        this.gameId = gameId;
        this.seatMap = seatMap;
    }

    @Override
    public void applyAllEvents(List<TableEvent> events) {
        for (TableEvent event : events) {
            aggregateVersion++;
            switch (event.getType()) {
            case TableCreated:
                applyEvent((TableCreatedEvent) event);
                break;
            default:
                throw new IllegalArgumentException("Event Type cannot be handled: "
                        + event.getType());
            }
        }
    }

    private void applyEvent(TableCreatedEvent event) {
        seatMap.putAll(event.getSeatPositionToPlayerMap());
    }

    public void createNewTable(Set<UUID> playerIds) {
        if (!seatMap.values().stream().allMatch(x -> x == null)) {
            throw new FlexPokerException("seatMap already contains players");
        }

        if (playerIds.size() < 2) {
            throw new FlexPokerException("must have at least two players");
        }

        if (playerIds.size() > seatMap.size()) {
            throw new FlexPokerException("player list can't be larger than seatMap");
        }

        Map<Integer, UUID> seatToPlayerMap = new HashMap<>();
        // TODO: randomize the seat placement
        List<UUID> playerIdsList = new ArrayList<>(playerIds);
        for (int i = 0; i < playerIds.size(); i++) {
            seatToPlayerMap.put(i, playerIdsList.get(i));
        }

        TableCreatedEvent tableCreatedEvent = new TableCreatedEvent(aggregateId,
                ++aggregateVersion, gameId, seatMap.size(), seatToPlayerMap);
        addNewEvent(tableCreatedEvent);
        applyEvent(tableCreatedEvent);
    }

}
