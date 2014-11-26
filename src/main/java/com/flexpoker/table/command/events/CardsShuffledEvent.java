package com.flexpoker.table.command.events;

import java.util.List;
import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.model.card.Card;
import com.flexpoker.table.command.framework.TableEvent;
import com.flexpoker.table.command.framework.TableEventType;

public class CardsShuffledEvent extends BaseEvent<TableEventType> implements TableEvent {

    private static final TableEventType TYPE = TableEventType.CardsShuffled;

    private final UUID gameId;

    private final List<Card> shuffledDeck;

    public CardsShuffledEvent(UUID aggregateId, int version, UUID gameId,
            List<Card> shuffledDeck) {
        super(aggregateId, version, TYPE);
        this.gameId = gameId;
        this.shuffledDeck = shuffledDeck;
    }

    @Override
    public UUID getGameId() {
        return gameId;
    }

    public List<Card> getShuffledDeck() {
        return shuffledDeck;
    }

}
