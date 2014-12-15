package com.flexpoker.table.query.repository.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.flexpoker.model.card.FlopCards;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.model.card.RiverCard;
import com.flexpoker.model.card.TurnCard;
import com.flexpoker.table.query.repository.CardsUsedInHandRepository;

@Repository
public class InMemoryCardsUsedInHandRepository implements CardsUsedInHandRepository {

    private final Map<UUID, FlopCards> handIdToFlopCardsMap;

    private final Map<UUID, TurnCard> handIdToTurnCardMap;

    private final Map<UUID, RiverCard> handIdToRiverCardMap;

    private final Map<UUID, Map<UUID, PocketCards>> handIdToPocketCardsMap;

    public InMemoryCardsUsedInHandRepository() {
        handIdToFlopCardsMap = new HashMap<>();
        handIdToTurnCardMap = new HashMap<>();
        handIdToRiverCardMap = new HashMap<>();
        handIdToPocketCardsMap = new HashMap<>();
    }

    @Override
    public void saveFlopCards(UUID handId, FlopCards flopCards) {
        handIdToFlopCardsMap.put(handId, flopCards);
    }

    @Override
    public void saveTurnCard(UUID handId, TurnCard turnCard) {
        handIdToTurnCardMap.put(handId, turnCard);
    }

    @Override
    public void saveRiverCard(UUID handId, RiverCard riverCard) {
        handIdToRiverCardMap.put(handId, riverCard);
    }

    @Override
    public void savePocketCards(UUID handId, Map<UUID, PocketCards> playerToPocketCardsMap) {
        handIdToPocketCardsMap.put(handId, playerToPocketCardsMap);
    }

    @Override
    public FlopCards fetchFlopCards(UUID handId) {
        return handIdToFlopCardsMap.get(handId);
    }

    @Override
    public TurnCard fetchTurnCard(UUID handId) {
        return handIdToTurnCardMap.get(handId);
    }

    @Override
    public RiverCard fetchRiverCard(UUID handId) {
        return handIdToRiverCardMap.get(handId);
    }

    @Override
    public PocketCards fetchPocketCards(UUID handId, UUID playerId) {
        return handIdToPocketCardsMap.get(handId).get(playerId);
    }

}
