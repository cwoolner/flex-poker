package com.flexpoker.table.command.events;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.model.card.Card;
import com.flexpoker.table.command.framework.TableEvent;

public class CardsShuffledEvent extends BaseEvent implements TableEvent {

    private final UUID gameId;

    private final List<Card> shuffledDeck;

    public CardsShuffledEvent(UUID aggregateId, UUID gameId, List<Card> shuffledDeck) {
        super(aggregateId);
        this.gameId = gameId;
        this.shuffledDeck = new ArrayList<>(shuffledDeck);
    }

    @JsonProperty
    @Override
    public UUID getGameId() {
        return gameId;
    }

    @JsonProperty
    public List<Card> getShuffledDeck() {
        return new ArrayList<>(shuffledDeck);
    }

}
