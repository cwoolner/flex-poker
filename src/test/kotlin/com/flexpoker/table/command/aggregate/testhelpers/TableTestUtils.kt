package com.flexpoker.table.command.aggregate.testhelpers

import com.flexpoker.table.command.Card
import com.flexpoker.table.command.CardRank
import com.flexpoker.table.command.HandRanking
import com.flexpoker.table.command.PocketCards
import com.flexpoker.table.command.aggregate.DefaultTableFactory
import com.flexpoker.table.command.aggregate.HandEvaluation
import com.flexpoker.table.command.aggregate.Table
import com.flexpoker.table.command.commands.CreateTableCommand
import com.flexpoker.table.command.events.HandDealtEvent
import com.flexpoker.table.command.events.TableCreatedEvent
import com.flexpoker.test.util.datageneration.CardGenerator.createPocketCards1
import com.flexpoker.test.util.datageneration.CardGenerator.createPocketCards2
import com.flexpoker.test.util.datageneration.DeckGenerator.createDeck
import java.util.ArrayList
import java.util.HashMap
import java.util.UUID

object TableTestUtils {

    fun createBasicTable(tableId: UUID, vararg playerIdsArray: UUID): Table {
        val playerIds = setOf(*playerIdsArray)
        val command = CreateTableCommand(tableId, UUID.randomUUID(), playerIds, 6)
        return DefaultTableFactory().createNew(command)
    }

    fun createBasicTableAndStartHand(tableId: UUID?, vararg playerIdsArray: UUID): Table {
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
        val table = DefaultTableFactory().createNew(command)
        table.startNewHandForNewGame(smallBlind, bigBlind, shuffledDeckOfCards, cardsUsedInHand, handEvaluations)
        return table
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

}