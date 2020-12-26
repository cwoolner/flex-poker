package com.flexpoker.table.command.aggregate.pot

import com.flexpoker.table.command.aggregate.HandEvaluation
import com.flexpoker.table.command.events.PotAmountIncreasedEvent
import com.flexpoker.table.command.events.PotClosedEvent
import com.flexpoker.table.command.events.PotCreatedEvent
import com.flexpoker.table.command.events.TableEvent
import org.pcollections.HashTreePMap
import org.pcollections.PMap
import java.util.HashMap
import java.util.HashSet
import java.util.Objects
import java.util.UUID
import java.util.function.Consumer

fun removePlayerFromAllPots(pots: MutableSet<Pot>, playerId: UUID) {
    pots.forEach { it.removePlayer(playerId) }
}

fun addToPot(pots: MutableSet<Pot>, potId: UUID, amountToAdd: Int) {
    pots.first { it.id == potId }.addChips(amountToAdd)
}

fun closePot(pots: MutableSet<Pot>, potId: UUID) {
    pots.first { it.id == potId }.closePot()
}

fun addNewPot(pots: MutableSet<Pot>, handEvaluationList: List<HandEvaluation>,
              potId: UUID, playersInvolved: Set<UUID?>) {
    val handEvaluationsOfPlayersInPot = handEvaluationList
        .filter { playersInvolved.contains(it.playerId) }
        .toSet()
    require(handEvaluationsOfPlayersInPot.isNotEmpty()) { "trying to add a new pot with players that are not part of the hand" }
    pots.add(Pot(potId, handEvaluationsOfPlayersInPot.toMutableSet()))
}

fun fetchPlayersRequiredToShowCards(pots: MutableSet<Pot>, playersStillInHand: Set<UUID>): Set<UUID> {
    return pots.fold(HashSet(), { acc, pot ->
        acc.addAll(playersStillInHand.filter { pot.forcePlayerToShowCards(it) })
        acc
    })
}

fun fetchChipsWon(pots: MutableSet<Pot>, playersStillInHand: Set<UUID>): PMap<UUID, Int> {
    val playersToChipsWonMap = HashMap<UUID, Int>()
    pots.forEach(Consumer { pot: Pot ->
        playersStillInHand.forEach(Consumer { playerInHand: UUID ->
            val numberOfChipsWonForPlayer = pot.getChipsWon(playerInHand)
            val existingChipsWon = playersToChipsWonMap.getOrDefault(playerInHand, 0)
            val newTotalOfChipsWon = numberOfChipsWonForPlayer + existingChipsWon
            playersToChipsWonMap[playerInHand] = newTotalOfChipsWon
        })
    })
    return HashTreePMap.from(playersToChipsWonMap)
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
 */
fun calculatePots(gameId: UUID, tableId: UUID, handId: UUID, pots: MutableSet<Pot>,
                  handEvaluationList: List<HandEvaluation>,
                  chipsInFrontMap: Map<UUID, Int>, chipsInBackMap: Map<UUID, Int>): List<TableEvent> {
    val newPotEvents = ArrayList<TableEvent>()
    val distinctChipsInFrontAmounts = chipsInFrontMap.values.filter { it != 0 }.distinct().sorted()
    var totalOfPreviousChipLevelIncreases = 0
    for (chipsPerLevel in distinctChipsInFrontAmounts) {
        val openPotOptional = pots.firstOrNull { it.isOpen }
        val openPotId = openPotOptional?.id ?: UUID.randomUUID()
        val playersAtThisChipLevel = chipsInFrontMap.filterValues { it >= chipsPerLevel }.map { it.key }.toSet()

        if (openPotOptional == null) {
            val potCreatedEvent = PotCreatedEvent(tableId, gameId, handId, openPotId, playersAtThisChipLevel)
            newPotEvents.add(potCreatedEvent)
            addNewPot(pots, handEvaluationList, potCreatedEvent.potId, potCreatedEvent.playersInvolved)
        }

        // subtract the total of the previous levels from the current level
        // before multiplying by the number of player, which will have the
        // same effect as actually reducing that amount from each player
        val increaseInChips = (chipsPerLevel - totalOfPreviousChipLevelIncreases) * playersAtThisChipLevel.size
        totalOfPreviousChipLevelIncreases += chipsPerLevel
        val potAmountIncreasedEvent = PotAmountIncreasedEvent(tableId, gameId, handId, openPotId, increaseInChips)
        newPotEvents.add(potAmountIncreasedEvent)
        addToPot(pots, potAmountIncreasedEvent.potId, potAmountIncreasedEvent.amountIncreased)

        // if a player bet, but no longer has any chips, then they are all
        // in and the pot should be closed
        if (playersAtThisChipLevel
                .filter { Objects.nonNull(it) }
                .filter { chipsInFrontMap[it]!! >= 1 }
                .filter { chipsInBackMap[it] == 0 }
                .count() > 0
        ) {
            val potClosedEvent = PotClosedEvent(tableId, gameId, handId, openPotId)
            newPotEvents.add(potClosedEvent)
            closePot(pots, potClosedEvent.potId)
        }
    }
    return newPotEvents
}