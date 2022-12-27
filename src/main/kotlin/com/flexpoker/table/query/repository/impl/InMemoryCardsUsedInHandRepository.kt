package com.flexpoker.table.query.repository.impl

import com.flexpoker.config.ProfileNames
import com.flexpoker.table.command.FlopCards
import com.flexpoker.table.command.PocketCards
import com.flexpoker.table.command.RiverCard
import com.flexpoker.table.command.TurnCard
import com.flexpoker.table.query.repository.CardsUsedInHandRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import java.util.UUID

@Profile(ProfileNames.DEFAULT, ProfileNames.TABLE_QUERY_INMEMORY)
@Repository
class InMemoryCardsUsedInHandRepository : CardsUsedInHandRepository {

    private val handIdToFlopCardsMap: MutableMap<UUID, FlopCards> = HashMap()
    private val handIdToTurnCardMap: MutableMap<UUID, TurnCard> = HashMap()
    private val handIdToRiverCardMap: MutableMap<UUID, RiverCard> = HashMap()
    private val handIdToPocketCardsMap: MutableMap<UUID, Map<UUID, PocketCards>> = HashMap()

    override fun saveFlopCards(handId: UUID, flopCards: FlopCards) {
        handIdToFlopCardsMap[handId] = flopCards
    }

    override fun saveTurnCard(handId: UUID, turnCard: TurnCard) {
        handIdToTurnCardMap[handId] = turnCard
    }

    override fun saveRiverCard(handId: UUID, riverCard: RiverCard) {
        handIdToRiverCardMap[handId] = riverCard
    }

    override fun savePocketCards(handId: UUID, playerToPocketCardsMap: Map<UUID, PocketCards>) {
        handIdToPocketCardsMap[handId] = playerToPocketCardsMap
    }

    override fun fetchFlopCards(handId: UUID): FlopCards {
        return handIdToFlopCardsMap[handId]!!
    }

    override fun fetchTurnCard(handId: UUID): TurnCard {
        return handIdToTurnCardMap[handId]!!
    }

    override fun fetchRiverCard(handId: UUID): RiverCard {
        return handIdToRiverCardMap[handId]!!
    }

    override fun fetchPocketCards(handId: UUID, playerId: UUID): PocketCards {
        return handIdToPocketCardsMap[handId]!![playerId]!!
    }

    override fun fetchAllPocketCardsForUser(playerId: UUID): Map<UUID, PocketCards> {
        return handIdToPocketCardsMap
            .filter { it.value.containsKey(playerId) }
            .mapValues { it.value[playerId]!! }
    }

}