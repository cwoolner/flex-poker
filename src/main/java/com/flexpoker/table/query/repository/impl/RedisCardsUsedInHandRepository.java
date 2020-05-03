package com.flexpoker.table.query.repository.impl;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.flexpoker.config.ProfileNames;
import com.flexpoker.model.card.FlopCards;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.model.card.RiverCard;
import com.flexpoker.model.card.TurnCard;
import com.flexpoker.table.query.repository.CardsUsedInHandRepository;

@Profile({ ProfileNames.REDIS, ProfileNames.TABLE_QUERY_REDIS })
@Repository
public class RedisCardsUsedInHandRepository implements CardsUsedInHandRepository {

    private static final String FLOP_CARDS_NAMESPACE = "flop-cards:";

    private static final String TURN_CARD_NAMESPACE = "turn-card:";

    private static final String RIVER_CARD_NAMESPACE = "river-card:";

    private static final String HAND_POCKET_CARDS_NAMESPACE = "hand-pocket-cards:";

    private final RedisTemplate<String, String> stringRedisTemplate;

    private final RedisTemplate<String, FlopCards> redisTemplateFlopCards;

    private final RedisTemplate<String, TurnCard> redisTemplateTurnCard;

    private final RedisTemplate<String, RiverCard> redisTemplateRiverCard;

    private final RedisTemplate<String, Map<UUID, PocketCards>> redisTemplatePocketCards;

    @Inject
    public RedisCardsUsedInHandRepository(
            RedisTemplate<String, String> stringRedisTemplate,
            RedisTemplate<String, FlopCards> redisTemplateFlopCards,
            RedisTemplate<String, TurnCard> redisTemplateTurnCard,
            RedisTemplate<String, RiverCard> redisTemplateRiverCard,
            RedisTemplate<String, Map<UUID, PocketCards>> redisTemplatePocketCards) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisTemplateFlopCards = redisTemplateFlopCards;
        this.redisTemplateTurnCard = redisTemplateTurnCard;
        this.redisTemplateRiverCard = redisTemplateRiverCard;
        this.redisTemplatePocketCards = redisTemplatePocketCards;
    }

    @Override
    public void saveFlopCards(UUID handId, FlopCards flopCards) {
        redisTemplateFlopCards.opsForValue().set(FLOP_CARDS_NAMESPACE + handId, flopCards);
    }

    @Override
    public void saveTurnCard(UUID handId, TurnCard turnCard) {
        redisTemplateTurnCard.opsForValue().set(TURN_CARD_NAMESPACE + handId, turnCard);
    }

    @Override
    public void saveRiverCard(UUID handId, RiverCard riverCard) {
        redisTemplateRiverCard.opsForValue().set(RIVER_CARD_NAMESPACE + handId, riverCard);
    }

    @Override
    public void savePocketCards(UUID handId, Map<UUID, PocketCards> playerToPocketCardsMap) {
        redisTemplatePocketCards.opsForHash().putAll(HAND_POCKET_CARDS_NAMESPACE + handId, playerToPocketCardsMap);
    }

    @Override
    public FlopCards fetchFlopCards(UUID handId) {
        return redisTemplateFlopCards.opsForValue().get(FLOP_CARDS_NAMESPACE + handId);
    }

    @Override
    public TurnCard fetchTurnCard(UUID handId) {
        return redisTemplateTurnCard.opsForValue().get(TURN_CARD_NAMESPACE + handId);
    }

    @Override
    public RiverCard fetchRiverCard(UUID handId) {
        return redisTemplateRiverCard.opsForValue().get(RIVER_CARD_NAMESPACE + handId);
    }

    @Override
    public PocketCards fetchPocketCards(UUID handId, UUID playerId) {
        return fetchPocketCardsForHand(handId).get(playerId);
    }

    @Override
    public Map<UUID, PocketCards> fetchAllPocketCardsForUser(UUID playerId) {
        return stringRedisTemplate.keys(HAND_POCKET_CARDS_NAMESPACE + "*")
                .stream()
                .map(keyString -> UUID.fromString(keyString.substring(HAND_POCKET_CARDS_NAMESPACE.length())))
                .collect(Collectors.toMap(
                        Function.identity(),
                        handId -> fetchPocketCardsForHand(handId)))
                .entrySet()
                .stream()
                .filter(x -> x.getValue().containsKey(playerId))
                .collect(Collectors.toMap(Map.Entry::getKey, x -> x.getValue().get(playerId)));
    }

    private Map<UUID, PocketCards> fetchPocketCardsForHand(UUID handId) {
        return redisTemplatePocketCards.opsForHash().entries(HAND_POCKET_CARDS_NAMESPACE + handId)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        x -> ((UUID) x.getKey()),
                        x -> (PocketCards) x.getValue()));
    }

}
