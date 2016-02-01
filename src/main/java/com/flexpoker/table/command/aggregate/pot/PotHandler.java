package com.flexpoker.table.command.aggregate.pot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.flexpoker.table.command.aggregate.HandEvaluation;
import com.flexpoker.table.command.events.PotAmountIncreasedEvent;
import com.flexpoker.table.command.events.PotClosedEvent;
import com.flexpoker.table.command.events.PotCreatedEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class PotHandler {

    private final Set<Pot> pots;

    private final UUID gameId;

    private final UUID tableId;

    private final UUID handId;

    private final List<HandEvaluation> handEvaluationList;

    public PotHandler(UUID gameId, UUID tableId, UUID handId,
            List<HandEvaluation> handEvaluationList) {
        this.gameId = gameId;
        this.tableId = tableId;
        this.handId = handId;
        this.handEvaluationList = handEvaluationList;
        pots = new HashSet<>();
    }

    public void removePlayerFromAllPots(UUID playerId) {
        pots.forEach(x -> x.removePlayer(playerId));
    }

    public void addToPot(UUID potId, int amountToAdd) {
        pots.stream()
                .filter(x -> x.getId().equals(potId))
                .findAny().get()
                .addChips(amountToAdd);
    }

    public void closePot(UUID potId) {
        pots.stream()
                .filter(x -> x.getId().equals(potId))
                .findAny().get()
                .closePot();
    }

    public void addNewPot(UUID potId, Set<UUID> playersInvolved) {
        Set<HandEvaluation> handEvaluationsOfPlayersInPot = handEvaluationList
                .stream().filter(x -> playersInvolved.contains(x.getPlayerId()))
                .collect(Collectors.toSet());
        if (handEvaluationsOfPlayersInPot.isEmpty()) {
            throw new IllegalArgumentException(
                    "trying to add a new pot with players that are not part of the hand");
        }
        pots.add(new Pot(potId, handEvaluationsOfPlayersInPot));
    }

    public Set<UUID> fetchPlayersRequriedToShowCards(Set<UUID> playersStillInHand) {
        Set<UUID> playersToShowCards = new HashSet<>();
        pots.forEach(pot -> {
            playersStillInHand.forEach(playerInHand -> {
                if (pot.forcePlayerToShowCards(playerInHand)) {
                    playersToShowCards.add(playerInHand);
                }
            });
        });
        return playersToShowCards;
    }

    public Map<UUID, Integer> fetchChipsWon(Set<UUID> playersStillInHand) {
        Map<UUID, Integer> playersToChipsWonMap = new HashMap<>();
        pots.forEach(pot -> {
            playersStillInHand.forEach(playerInHand -> {
                int numberOfChipsWonForPlayer = pot.getChipsWon(playerInHand);
                int existingChipsWon = playersToChipsWonMap.getOrDefault(playerInHand, 0);
                int newTotalOfChipsWon = numberOfChipsWonForPlayer + existingChipsWon;
                playersToChipsWonMap.put(playerInHand, newTotalOfChipsWon);
            });
        });
        return playersToChipsWonMap;
    }

    /**
     * The general approach to calculating pots is as follows:
     * 
     * 1. Discover all of the distinct numbers of chips in front of each player.
     * For example, if everyone has 30 chips in front, 30 would be the only
     * number in the distinct set. If two players had 10 and one person had 20,
     * then 10 and 20 would be in the set.
     * 
     * 2. Loop through each chip count, starting with the smallest, and shave
     * off the number of chips from each stack in front of each player, and
     * place them into an open pot.
     * 
     * 3. If an open pot does not exist, create a new one.
     * 
     * 4. If it's determined that a player is all-in, then the pot for that
     * player's all-in should be closed. Multiple closed pots can exist, but
     * only one open pot should ever exist at any given time.
     * 
     * @param aggregateVersion
     * @param playersStillInHand
     * @param chipsInFrontMap
     */
    public List<TableEvent> calculatePots(int aggregateVersion,
            Map<UUID, Integer> chipsInFrontMap,
            Map<UUID, Integer> chipsInBackMap,
            Set<UUID> playersStillInHand) {
        List<TableEvent> newPotEvents = new ArrayList<>();

        List<Integer> distinctChipsInFrontAmounts = chipsInFrontMap.values().stream()
                .filter(x -> x.intValue() != 0)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        int totalOfPreviousChipLevelIncreases = 0;
        
        for (int chipsPerLevel : distinctChipsInFrontAmounts) {
            Optional<Pot> openPotOptional = pots.stream()
                    .filter(x -> x.isOpen()).findAny();

            UUID openPotId = openPotOptional.isPresent()
                    ? openPotOptional.get().getId() : UUID.randomUUID();

            Set<UUID> playersAtThisChipLevel = chipsInFrontMap.entrySet().stream()
                    .filter(x -> x.getValue() >= chipsPerLevel)
                    .map(x -> x.getKey())
                    .collect(Collectors.toSet());

            if (!openPotOptional.isPresent()) {
                PotCreatedEvent potCreatedEvent = new PotCreatedEvent(tableId,
                        aggregateVersion, gameId, handId, openPotId, playersAtThisChipLevel);
                newPotEvents.add(potCreatedEvent);
                addNewPot(potCreatedEvent.getPotId(),
                        potCreatedEvent.getPlayersInvolved());
            }

            // subtract the total of the previous levels from the current level
            // before multiplying by the number of player, which will have the
            // same effect as actually reducing that amount from each player
            int increaseInChips = (chipsPerLevel - totalOfPreviousChipLevelIncreases) * playersAtThisChipLevel.size();
            totalOfPreviousChipLevelIncreases += chipsPerLevel;
            
            PotAmountIncreasedEvent potAmountIncreasedEvent = new PotAmountIncreasedEvent(
                    tableId, aggregateVersion + newPotEvents.size(), gameId,
                    handId, openPotId, increaseInChips);
            newPotEvents.add(potAmountIncreasedEvent);
            addToPot(potAmountIncreasedEvent.getPotId(),
                    potAmountIncreasedEvent.getAmountIncreased());

            // if a player bet, but no longer has any chips, then they are all
            // in and the pot should be closed
            if (playersAtThisChipLevel.stream()
                    .filter(Objects::nonNull)
                    .filter(player -> chipsInFrontMap.get(player) >= 1)
                    .filter(player -> chipsInBackMap.get(player) == 0)
                    .count() > 0) {
                PotClosedEvent potClosedEvent = new PotClosedEvent(tableId,
                        aggregateVersion, gameId, handId, openPotId);
                newPotEvents.add(potClosedEvent);
                closePot(potClosedEvent.getPotId());
            };
        }

        return newPotEvents;
    }

}
