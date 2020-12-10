package com.flexpoker.table.query.repository;

import java.util.Map;
import java.util.UUID;

import com.flexpoker.table.command.FlopCards;
import com.flexpoker.table.command.PocketCards;
import com.flexpoker.table.command.RiverCard;
import com.flexpoker.table.command.TurnCard;

public interface CardsUsedInHandRepository {

    void saveFlopCards(UUID handId, FlopCards flopCards);

    void saveTurnCard(UUID handId, TurnCard turnCard);

    void saveRiverCard(UUID handId, RiverCard riverCard);

    void savePocketCards(UUID handId, Map<UUID, PocketCards> playerToPocketCardsMap);

    FlopCards fetchFlopCards(UUID handId);

    TurnCard fetchTurnCard(UUID handId);

    RiverCard fetchRiverCard(UUID handId);

    PocketCards fetchPocketCards(UUID handId, UUID playerId);

    Map<UUID, PocketCards> fetchAllPocketCardsForUser(UUID playerId);

}
