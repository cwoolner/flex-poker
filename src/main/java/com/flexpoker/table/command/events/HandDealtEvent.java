package com.flexpoker.table.command.events;

import java.util.Map;
import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.model.card.FlopCards;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.model.card.RiverCard;
import com.flexpoker.model.card.TurnCard;
import com.flexpoker.table.command.framework.TableEvent;
import com.flexpoker.table.command.framework.TableEventType;

public class HandDealtEvent extends BaseEvent<TableEventType> implements TableEvent {

    private static final TableEventType TYPE = TableEventType.HandDealtEvent;

    private final UUID gameId;

    private final FlopCards flopCards;

    private final TurnCard turnCard;

    private final RiverCard riverCard;

    private final int buttonOnPosition;

    private final int smallBlindPosition;

    private final int bigBlindPosition;

    private final int actionOnPosition;

    private final Map<Integer, PocketCards> positionToPocketCardsMap;

    public HandDealtEvent(UUID aggregateId, int version, UUID gameId,
            FlopCards flopCards, TurnCard turnCard, RiverCard riverCard,
            int buttonOnPosition, int smallBlindPosition, int bigBlindPosition,
            int actionOnPosition, Map<Integer, PocketCards> positionToPocketCardsMap) {
        super(aggregateId, version, TYPE);
        this.gameId = gameId;
        this.flopCards = flopCards;
        this.turnCard = turnCard;
        this.riverCard = riverCard;
        this.buttonOnPosition = buttonOnPosition;
        this.smallBlindPosition = smallBlindPosition;
        this.bigBlindPosition = bigBlindPosition;
        this.actionOnPosition = actionOnPosition;
        this.positionToPocketCardsMap = positionToPocketCardsMap;
    }

    @Override
    public UUID getGameId() {
        return gameId;
    }

    public FlopCards getFlopCards() {
        return flopCards;
    }

    public TurnCard getTurnCard() {
        return turnCard;
    }

    public RiverCard getRiverCard() {
        return riverCard;
    }

    public int getButtonOnPosition() {
        return buttonOnPosition;
    }

    public int getSmallBlindPosition() {
        return smallBlindPosition;
    }

    public int getBigBlindPosition() {
        return bigBlindPosition;
    }

    public int getActionOnPosition() {
        return actionOnPosition;
    }

    public Map<Integer, PocketCards> getPositionToPocketCardsMap() {
        return positionToPocketCardsMap;
    }

}
