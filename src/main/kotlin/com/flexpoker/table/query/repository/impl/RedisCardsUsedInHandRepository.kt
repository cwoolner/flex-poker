package com.flexpoker.table.query.repository.impl

import com.flexpoker.config.ProfileNames
import com.flexpoker.table.command.FlopCards
import com.flexpoker.table.command.PocketCards
import com.flexpoker.table.command.RiverCard
import com.flexpoker.table.command.TurnCard
import com.flexpoker.table.query.repository.CardsUsedInHandRepository
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ScanOptions
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
    private val redisTemplatePocketCards: RedisTemplate<String, Map<UUID, PocketCards>>,
) : CardsUsedInHandRepository {

    companion object {
        private const val FLOP_CARDS_NAMESPACE = "flop-cards"
        private const val TURN_CARD_NAMESPACE = "turn-card"
        private const val RIVER_CARD_NAMESPACE = "river-card"
        private const val HAND_POCKET_CARDS_NAMESPACE = "hand-pocket-cards"

        private val HAND_POCKET_CARDS_SCAN_OPTIONS = ScanOptions.scanOptions()
            .match("$HAND_POCKET_CARDS_NAMESPACE:*")
            .count(100)
            .build()
    }

    private fun flopCardsRedisKey(handId: UUID) = "$FLOP_CARDS_NAMESPACE:$handId"
    private fun turnCardRedisKey(handId: UUID) = "$TURN_CARD_NAMESPACE:$handId"
    private fun riverCardRedisKey(handId: UUID) = "$RIVER_CARD_NAMESPACE:$handId"
    private fun handPocketCardsRedisKey(handId: UUID) = "$HAND_POCKET_CARDS_NAMESPACE:$handId"

    override fun saveFlopCards(handId: UUID, flopCards: FlopCards) {
        redisTemplateFlopCards.opsForValue()[flopCardsRedisKey(handId)] = flopCards
    }

    override fun saveTurnCard(handId: UUID, turnCard: TurnCard) {
        redisTemplateTurnCard.opsForValue()[turnCardRedisKey(handId)] = turnCard
    }

    override fun saveRiverCard(handId: UUID, riverCard: RiverCard) {
        redisTemplateRiverCard.opsForValue()[riverCardRedisKey(handId)] = riverCard
    }

    override fun savePocketCards(handId: UUID, playerToPocketCardsMap: Map<UUID, PocketCards>) {
        redisTemplatePocketCards.opsForHash<Any, Any>()
            .putAll(handPocketCardsRedisKey(handId), playerToPocketCardsMap)
    }

    override fun fetchFlopCards(handId: UUID): FlopCards? {
        return redisTemplateFlopCards.opsForValue()[flopCardsRedisKey(handId)]
    }

    override fun fetchTurnCard(handId: UUID): TurnCard? {
        return redisTemplateTurnCard.opsForValue()[turnCardRedisKey(handId)]
    }

    override fun fetchRiverCard(handId: UUID): RiverCard? {
        return redisTemplateRiverCard.opsForValue()[riverCardRedisKey(handId)]
    }

    override fun fetchPocketCards(handId: UUID, playerId: UUID): PocketCards? {
        return fetchPocketCardsForHand(handId)[playerId]
    }

    override fun fetchAllPocketCardsForUser(playerId: UUID): Map<UUID, PocketCards> {
        val cursor = stringRedisTemplate.scan(HAND_POCKET_CARDS_SCAN_OPTIONS)

        val keys = mutableListOf<String>()
        while (cursor.hasNext()) {
            keys.add(cursor.next())
        }

        return keys
            .associateWith { redisTemplatePocketCards.opsForHash<UUID, PocketCards>().get(it, playerId) }
            .filter { it.value != null }
            .map { (key, value) -> UUID.fromString(key.substring(HAND_POCKET_CARDS_NAMESPACE.length.inc())) to value!! }
            .toMap()
    }

    override fun removeHand(handId: UUID) {
        redisTemplateFlopCards.delete(flopCardsRedisKey(handId))
        redisTemplateTurnCard.delete(turnCardRedisKey(handId))
        redisTemplateRiverCard.delete(riverCardRedisKey(handId))
        redisTemplatePocketCards.delete(handPocketCardsRedisKey(handId))
    }

    private fun fetchPocketCardsForHand(handId: UUID): Map<UUID, PocketCards> {
        return redisTemplatePocketCards.opsForHash<UUID, PocketCards>()
            .entries(handPocketCardsRedisKey(handId))
    }

}