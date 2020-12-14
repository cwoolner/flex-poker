package com.flexpoker.table.query.repository.impl

import com.flexpoker.config.ProfileNames
import com.flexpoker.table.command.FlopCards
import com.flexpoker.table.command.PocketCards
import com.flexpoker.table.command.RiverCard
import com.flexpoker.table.command.TurnCard
import com.flexpoker.table.query.repository.CardsUsedInHandRepository
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.UUID
import javax.inject.Inject

@Profile(ProfileNames.REDIS, ProfileNames.TABLE_QUERY_REDIS)
@Repository
class RedisCardsUsedInHandRepository @Inject constructor(
    private val stringRedisTemplate: RedisTemplate<String, String>,
    private val redisTemplateFlopCards: RedisTemplate<String, FlopCards>,
    private val redisTemplateTurnCard: RedisTemplate<String, TurnCard>,
    private val redisTemplateRiverCard: RedisTemplate<String, RiverCard>,
    private val redisTemplatePocketCards: RedisTemplate<String, Map<UUID, PocketCards>>
) : CardsUsedInHandRepository {

    companion object {
        private const val FLOP_CARDS_NAMESPACE = "flop-cards:"
        private const val TURN_CARD_NAMESPACE = "turn-card:"
        private const val RIVER_CARD_NAMESPACE = "river-card:"
        private const val HAND_POCKET_CARDS_NAMESPACE = "hand-pocket-cards:"
    }

    override fun saveFlopCards(handId: UUID, flopCards: FlopCards) {
        redisTemplateFlopCards.opsForValue()[FLOP_CARDS_NAMESPACE + handId] = flopCards
    }

    override fun saveTurnCard(handId: UUID, turnCard: TurnCard) {
        redisTemplateTurnCard.opsForValue()[TURN_CARD_NAMESPACE + handId] = turnCard
    }

    override fun saveRiverCard(handId: UUID, riverCard: RiverCard) {
        redisTemplateRiverCard.opsForValue()[RIVER_CARD_NAMESPACE + handId] = riverCard
    }

    override fun savePocketCards(handId: UUID, playerToPocketCardsMap: Map<UUID, PocketCards>) {
        redisTemplatePocketCards.opsForHash<Any, Any>()
            .putAll(HAND_POCKET_CARDS_NAMESPACE + handId, playerToPocketCardsMap)
    }

    override fun fetchFlopCards(handId: UUID): FlopCards {
        return redisTemplateFlopCards.opsForValue()[FLOP_CARDS_NAMESPACE + handId]
    }

    override fun fetchTurnCard(handId: UUID): TurnCard {
        return redisTemplateTurnCard.opsForValue()[TURN_CARD_NAMESPACE + handId]
    }

    override fun fetchRiverCard(handId: UUID): RiverCard {
        return redisTemplateRiverCard.opsForValue()[RIVER_CARD_NAMESPACE + handId]
    }

    override fun fetchPocketCards(handId: UUID, playerId: UUID): PocketCards {
        return fetchPocketCardsForHand(handId)[playerId]!!
    }

    override fun fetchAllPocketCardsForUser(playerId: UUID): Map<UUID, PocketCards> {
        return stringRedisTemplate
            .keys("$HAND_POCKET_CARDS_NAMESPACE*")
            .map { it.substring(HAND_POCKET_CARDS_NAMESPACE.length) }
            .map { UUID.fromString(it) }
            .associateWith { fetchPocketCardsForHand(it) }
            .filter { it.value.containsKey(playerId) }
            .mapValues { it.value[playerId]!! }
    }

    private fun fetchPocketCardsForHand(handId: UUID): Map<UUID, PocketCards> {
        return redisTemplatePocketCards
            .opsForHash<Any, Any>().entries(HAND_POCKET_CARDS_NAMESPACE + handId) as Map<UUID, PocketCards>
    }

}