package com.flexpoker.table.command.aggregate

import com.flexpoker.table.command.Card
import com.flexpoker.table.command.CardRank
import com.flexpoker.table.command.CardSuit
import com.flexpoker.table.command.CardsUsedInHand
import com.flexpoker.table.command.FlopCards
import com.flexpoker.table.command.HandDealerState
import com.flexpoker.table.command.PlayerAction
import com.flexpoker.table.command.PocketCards
import com.flexpoker.table.command.RiverCard
import com.flexpoker.table.command.TurnCard
import com.flexpoker.table.command.aggregate.eventproducers.hand.checkActionOnPlayer
import com.flexpoker.table.command.aggregate.eventproducers.hand.checkPerformAction
import com.flexpoker.test.util.UnitTestClass
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.pcollections.HashTreePMap
import org.pcollections.HashTreePSet
import org.pcollections.TreePVector
import java.util.UUID

@UnitTestClass
class CheckActionOnPlayerTest {

    @Test
    fun `the action is on the given player`() {
        val playerId = UUID.randomUUID()
        val state = genericHandState().copy(
            actionOnPosition = 0,
            seatMap = HashTreePMap.singleton(0, playerId)
        )
        checkActionOnPlayer(state, playerId)
    }

    @Test
    fun `the action is not on the given player`() {
        val playerId = UUID.randomUUID()
        val state = genericHandState().copy(
            actionOnPosition = 1,
            seatMap = HashTreePMap.singleton(0, playerId)
        )
        assertThrows(IllegalArgumentException::class.java) { checkActionOnPlayer(state, playerId) }
    }

}

@UnitTestClass
class CheckPerformAction {

    @Test
    fun `player is allowed to perform action`() {
        val playerId = UUID.randomUUID()
        val state = genericHandState().copy(
            possibleSeatActionsMap = HashTreePMap.singleton(playerId, setOf(PlayerAction.CHECK, PlayerAction.FOLD))
        )
        checkPerformAction(state, playerId, PlayerAction.CHECK)
    }

    @Test
    fun `player is not allowed to perform action`() {
        val playerId = UUID.randomUUID()
        val state = genericHandState().copy(
            possibleSeatActionsMap = HashTreePMap.singleton(playerId, setOf(PlayerAction.CALL, PlayerAction.FOLD))
        )
        assertThrows(IllegalArgumentException::class.java) { checkPerformAction(state, playerId, PlayerAction.CHECK) }
    }

}

fun genericHandState(): HandState {
    val pocketCards = PocketCards(
        Card(0, CardRank.NINE, CardSuit.CLUBS),
        Card(0, CardRank.EIGHT, CardSuit.CLUBS)
    )
    val cardsUsedInHand = CardsUsedInHand(
        FlopCards(
            Card(0, CardRank.ACE, CardSuit.CLUBS),
            Card(0, CardRank.KING, CardSuit.CLUBS),
            Card(0, CardRank.QUEEN, CardSuit.CLUBS)),
        TurnCard(Card(0, CardRank.JACK, CardSuit.CLUBS)),
        RiverCard(Card(0, CardRank.TEN, CardSuit.CLUBS)),
        listOf(pocketCards)
    )
    val playerId = UUID.randomUUID()
    val playerToPocketCardsMap = HashTreePMap.singleton(playerId, pocketCards)
    val possibleSeatActionsMap = HashTreePMap.singleton(playerId, setOf(PlayerAction.CHECK))
    val playersStillInHand = HashTreePSet.singleton(playerId)
    val chipsInBack = HashTreePMap.singleton(playerId, 1500)
    return HandState(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), HashTreePMap.empty(),
        cardsUsedInHand.flopCards, cardsUsedInHand.turnCard,
        cardsUsedInHand.riverCard, 0, 0, 1, null, playerToPocketCardsMap,
        possibleSeatActionsMap, playersStillInHand, TreePVector.empty(),
        HandDealerState.NONE, chipsInBack, HashTreePMap.empty(), HashTreePMap.empty(),
        HashTreePMap.empty(), 10, 20, 0, null,
        HashTreePSet.empty(), false, false, false, HashTreePSet.empty())
}