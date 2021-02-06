package com.flexpoker.table.command.aggregate

import com.flexpoker.table.command.events.PotAmountIncreasedEvent
import com.flexpoker.table.command.events.PotClosedEvent
import com.flexpoker.table.command.events.PotCreatedEvent
import com.flexpoker.table.command.events.TableEvent
import com.flexpoker.util.toPMap
import com.flexpoker.util.toPSet
import org.pcollections.PMap
import org.pcollections.PSet
import java.util.HashMap
import java.util.HashSet
import java.util.UUID
import java.util.function.Consumer

fun removePlayerFromAllPots(pots: PSet<PotState>, playerId: UUID): PSet<PotState> {
    require(pots
        .filter { it.handEvaluations.any { x -> x.playerId == playerId } }
        .all { it.isOpen }) { "cannot remove player from a closed pot" }
    return pots
        .map { it.copy(handEvaluations = it.handEvaluations.filter { he -> he.playerId != playerId }.toPSet()) }
        .toPSet()
}

fun addToPot(pots: PSet<PotState>, potId: UUID, amountToAdd: Int): PSet<PotState> {
    return pots.map {
        if (it.id == potId) {
            require(it.isOpen) { "cannot add chips to a closed pot" }
            it.copy(amount = it.amount + amountToAdd)
        } else {
            it
        }
    }.toPSet()
}

fun closePot(pots: PSet<PotState>, potId: UUID): PSet<PotState> {
    val updatedPots = pots.map { if (it.id == potId) it.copy(isOpen = false) else it }.toPSet()
    require(updatedPots.isNotEmpty()) { "attempting to close pot that does not exist" }
    return updatedPots
}

fun addNewPot(pots: PSet<PotState>, handEvaluationList: List<HandEvaluation>,
              potId: UUID, playersInvolved: Set<UUID?>): PSet<PotState> {
    val handEvaluationsOfPlayersInPot = handEvaluationList.filter { playersInvolved.contains(it.playerId) }.toPSet()
    require(handEvaluationsOfPlayersInPot.isNotEmpty()) { "trying to add a new pot with players that are not part of the hand" }
    return pots.plus(PotState(potId, 0, true, handEvaluationsOfPlayersInPot))
}

fun fetchPlayersRequiredToShowCards(pots: PSet<PotState>, playersStillInHand: Set<UUID>): Set<UUID> {
    return pots.fold(HashSet(), { acc, pot ->
        acc.addAll(playersStillInHand.filter { forcePlayerToShowCards(pot, it) })
        acc
    })
}

fun fetchChipsWon(pots: PSet<PotState>, playersStillInHand: Set<UUID>): PMap<UUID, Int> {
    val playersToChipsWonMap = HashMap<UUID, Int>()
    pots.forEach(Consumer { pot: PotState ->
        playersStillInHand.forEach(Consumer { playerInHand: UUID ->
            val numberOfChipsWonForPlayer = chipsWon(pot, playerInHand)
            val existingChipsWon = playersToChipsWonMap.getOrDefault(playerInHand, 0)
            val newTotalOfChipsWon = numberOfChipsWonForPlayer + existingChipsWon
            playersToChipsWonMap[playerInHand] = newTotalOfChipsWon
        })
    })
    return playersToChipsWonMap.toPMap()
}

fun forcePlayerToShowCards(pot: PotState, playerInHand: UUID): Boolean {
    return chipsWon(pot, playerInHand) > 0 && playersInvolved(pot).size > 1
}

private fun chipsWon(pot: PotState, playerInHand: UUID): Int {
    return recalculateWinners(pot).getOrDefault(playerInHand, 0)
}

private fun playersInvolved(pot: PotState): PSet<UUID> {
    return pot.handEvaluations.map { it.playerId!! }.toPSet()
}

private fun recalculateWinners(pot: PotState): PMap<UUID, Int> {
    val chipsForPlayerToWin = mutableMapOf<UUID, Int>()
    val playersInvolved = playersInvolved(pot)

    val relevantHandEvaluationsForPot = pot.handEvaluations
        .filter { playersInvolved.contains(it.playerId) }
        .sortedDescending()

    val winners = relevantHandEvaluationsForPot
        .fold(ArrayList<HandEvaluation>(), { acc, handEvaluation ->
            if (acc.isEmpty() || acc.first() <= handEvaluation ) {
                acc.add(handEvaluation)
            }
            acc
        })
        .map { it.playerId!! }

    val numberOfWinners = winners.size
    val baseNumberOfChips = pot.amount / numberOfWinners
    val bonusChips = pot.amount % numberOfWinners
    winners.forEach { chipsForPlayerToWin[it] = baseNumberOfChips }
    if (bonusChips >= 1) {
        val randomNumber = DefaultRandomNumberGenerator().pseudoRandomIntBasedOnUUID(pot.id, winners.size)
        chipsForPlayerToWin.compute(winners[randomNumber]) { _: UUID, chips: Int? -> chips!! + bonusChips }
    }
    return chipsForPlayerToWin.toPMap()
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
fun calculatePots(gameId: UUID, tableId: UUID, handId: UUID, pots: PSet<PotState>,
                  handEvaluationList: List<HandEvaluation>, chipsInFrontMap: Map<UUID, Int>,
                  chipsInBackMap: Map<UUID, Int>): Pair<List<TableEvent>, PSet<PotState>> {
    var updatedPots = pots
    val newPotEvents = ArrayList<TableEvent>()
    val distinctChipsInFrontAmounts = chipsInFrontMap.values.filter { it != 0 }.distinct().sorted()
    var totalOfPreviousChipLevelIncreases = 0
    for (chipsPerLevel in distinctChipsInFrontAmounts) {
        val openPotOptional = updatedPots.firstOrNull { it.isOpen }
        val openPotId = openPotOptional?.id ?: UUID.randomUUID()
        val playersAtThisChipLevel = chipsInFrontMap.filterValues { it >= chipsPerLevel }.map { it.key }.toSet()

        if (openPotOptional == null) {
            val potCreatedEvent = PotCreatedEvent(tableId, gameId, handId, openPotId, playersAtThisChipLevel)
            newPotEvents.add(potCreatedEvent)
            updatedPots = addNewPot(updatedPots, handEvaluationList, potCreatedEvent.potId, potCreatedEvent.playersInvolved)
        }

        // subtract the total of the previous levels from the current level
        // before multiplying by the number of player, which will have the
        // same effect as actually reducing that amount from each player
        val increaseInChips = (chipsPerLevel - totalOfPreviousChipLevelIncreases) * playersAtThisChipLevel.size
        totalOfPreviousChipLevelIncreases += chipsPerLevel
        val potAmountIncreasedEvent = PotAmountIncreasedEvent(tableId, gameId, handId, openPotId, increaseInChips)
        newPotEvents.add(potAmountIncreasedEvent)
        updatedPots = addToPot(updatedPots, potAmountIncreasedEvent.potId, potAmountIncreasedEvent.amountIncreased)

        // if a player bet, but no longer has any chips, then they are all
        // in and the pot should be closed
        if (playersAtThisChipLevel
                .filter { chipsInFrontMap[it]!! >= 1 }
                .filter { chipsInBackMap[it] == 0 }
                .count() > 0) {
            val potClosedEvent = PotClosedEvent(tableId, gameId, handId, openPotId)
            newPotEvents.add(potClosedEvent)
            updatedPots = closePot(updatedPots, potClosedEvent.potId)
        }
    }
    return Pair(newPotEvents, updatedPots)
}