package com.flexpoker.table.command.aggregate.pot

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.table.command.aggregate.HandEvaluation
import java.util.ArrayList
import java.util.HashMap
import java.util.Random
import java.util.UUID

class Pot(val id: UUID, private val handEvaluations: MutableSet<HandEvaluation>) {
    private val playersInvolved = handEvaluations.map { it.playerId }.toMutableSet()
    private val chipsForPlayerToWin = HashMap<UUID, Int>()
    private var amount = 0
    var isOpen = true
        private set

    fun getChipsWon(playerInHand: UUID): Int {
        return chipsForPlayerToWin.getOrDefault(playerInHand, Integer.valueOf(0))
    }

    fun forcePlayerToShowCards(playerInHand: UUID): Boolean {
        return getChipsWon(playerInHand) > 0 && playersInvolved.size > 1
    }

    fun addChips(chips: Int) {
        if (!isOpen) {
            throw FlexPokerException("cannot add chips to a closed pot")
        }
        amount += chips
        recalculateWinners()
    }

    fun removePlayer(playerId: UUID) {
        if (!isOpen) {
            throw FlexPokerException("cannot remove player from a closed pot")
        }
        playersInvolved.remove(playerId)
        handEvaluations.removeIf { it.playerId == playerId }
        recalculateWinners()
    }

    fun closePot() {
        isOpen = false
    }

    private fun recalculateWinners() {
        val relevantHandEvaluationsForPot = handEvaluations
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
        val baseNumberOfChips = amount / numberOfWinners
        val bonusChips = amount % numberOfWinners
        chipsForPlayerToWin.clear()
        winners.forEach { chipsForPlayerToWin[it] = baseNumberOfChips }
        if (bonusChips >= 1) {
            val randomNumber = Random(System.currentTimeMillis()).nextInt(winners.size)
            chipsForPlayerToWin.compute(winners[randomNumber]) { _: UUID, chips: Int? -> chips!! + bonusChips }
        }
    }

}