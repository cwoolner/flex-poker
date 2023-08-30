package com.flexpoker.table.query.repository

import com.flexpoker.table.command.FlopCards
import com.flexpoker.table.command.PocketCards
import com.flexpoker.table.command.RiverCard
import com.flexpoker.table.command.TurnCard
import java.util.UUID

interface CardsUsedInHandRepository {
    fun saveFlopCards(handId: UUID, flopCards: FlopCards)
    fun saveTurnCard(handId: UUID, turnCard: TurnCard)
    fun saveRiverCard(handId: UUID, riverCard: RiverCard)
    fun savePocketCards(handId: UUID, playerToPocketCardsMap: Map<UUID, PocketCards>)
    fun fetchFlopCards(handId: UUID): FlopCards?
    fun fetchTurnCard(handId: UUID): TurnCard?
    fun fetchRiverCard(handId: UUID): RiverCard?
    fun fetchPocketCards(handId: UUID, playerId: UUID): PocketCards?
    fun fetchAllPocketCardsForUser(playerId: UUID): Map<UUID, PocketCards>
    fun removeHand(handId: UUID)
}