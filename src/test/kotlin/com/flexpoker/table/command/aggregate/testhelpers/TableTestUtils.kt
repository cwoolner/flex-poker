package com.flexpoker.table.command.aggregate.testhelpers

import com.flexpoker.table.command.Card
import com.flexpoker.table.command.CardRank
import com.flexpoker.table.command.HandRanking
import com.flexpoker.table.command.PocketCards
import com.flexpoker.table.command.aggregate.DefaultRandomNumberGenerator
import com.flexpoker.table.command.aggregate.HandEvaluation
import com.flexpoker.table.command.aggregate.TableState
import com.flexpoker.table.command.aggregate.applyEvents
import com.flexpoker.table.command.aggregate.eventproducers.createTable
import com.flexpoker.table.command.aggregate.eventproducers.startNewHandForNewGame
import com.flexpoker.table.command.commands.CreateTableCommand
import com.flexpoker.table.command.events.HandDealtEvent
import com.flexpoker.table.command.events.TableCreatedEvent
import com.flexpoker.table.command.events.TableEvent
import com.flexpoker.test.util.datageneration.CardGenerator.createPocketCards1
import com.flexpoker.test.util.datageneration.CardGenerator.createPocketCards2
import com.flexpoker.test.util.datageneration.DeckGenerator.createDeck
import java.util.UUID

fun createBasicTable(tableId: UUID, vararg playerIdsArray: UUID): TableState {
    val playerIds = setOf(*playerIdsArray)
    val command = CreateTableCommand(tableId, UUID.randomUUID(), playerIds, 6)
    return applyEvents(createTable(command.tableId, command.gameId, command.numberOfPlayersPerTable,
        command.playerIds, DefaultRandomNumberGenerator()))
}

fun createBasicTableAndStartHand(tableId: UUID?, vararg playerIdsArray: UUID): List<TableEvent> {
    val playerIds = setOf(*playerIdsArray)
    val smallBlind = 10
    val bigBlind = 20
    val shuffledDeckOfCards = ArrayList<Card>()
    val cardsUsedInHand = createDeck()
    val handEvaluation1 = HandEvaluation()
    handEvaluation1.playerId = playerIdsArray[0]
    handEvaluation1.handRanking = HandRanking.FLUSH
    handEvaluation1.primaryCardRank = CardRank.KING
    val handEvaluation2 = HandEvaluation()
    handEvaluation2.playerId = playerIdsArray[1]
    handEvaluation2.handRanking = HandRanking.STRAIGHT
    handEvaluation2.primaryCardRank = CardRank.KING
    val handEvaluations = HashMap<PocketCards, HandEvaluation>()
    handEvaluations[createPocketCards1()] = handEvaluation1
    handEvaluations[createPocketCards2()] = handEvaluation2

    val command = CreateTableCommand(tableId!!, UUID.randomUUID(), playerIds, 6)
    val initEvents = createTable(command.tableId, command.gameId, command.numberOfPlayersPerTable,
        command.playerIds, DefaultRandomNumberGenerator())
    val initState = applyEvents(initEvents)
    val events = startNewHandForNewGame(initState, smallBlind, bigBlind, shuffledDeckOfCards, cardsUsedInHand,
        handEvaluations, DefaultRandomNumberGenerator())

    return initEvents + events
}

fun fetchIdForButton(tableCreatedEvent: TableCreatedEvent, handDealtEvent: HandDealtEvent): UUID {
    val seatPositionToPlayerIdMap = tableCreatedEvent.seatPositionToPlayerMap
    return seatPositionToPlayerIdMap[handDealtEvent.buttonOnPosition]!!
}

fun fetchIdForSmallBlind(tableCreatedEvent: TableCreatedEvent, handDealtEvent: HandDealtEvent): UUID {
    val seatPositionToPlayerIdMap = tableCreatedEvent.seatPositionToPlayerMap
    return seatPositionToPlayerIdMap[handDealtEvent.smallBlindPosition]!!
}

fun fetchIdForBigBlind(tableCreatedEvent: TableCreatedEvent, handDealtEvent: HandDealtEvent): UUID {
    val seatPositionToPlayerIdMap = tableCreatedEvent.seatPositionToPlayerMap
    return seatPositionToPlayerIdMap[handDealtEvent.bigBlindPosition]!!
}

fun blindPlayerIds(state: TableState): Triple<UUID, UUID, UUID> {
    return Triple(
        state.seatMap.entries.first { it.key == state.buttonOnPosition }.value!!,
        state.seatMap.entries.first { it.key == state.smallBlindPosition }.value!!,
        state.seatMap.entries.first { it.key == state.bigBlindPosition }.value!!
    )
}
