package com.flexpoker.table.command.aggregate

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.table.command.commands.CreateTableCommand
import com.flexpoker.table.command.events.TableCreatedEvent
import com.flexpoker.table.command.events.TableEvent
import com.flexpoker.table.command.factory.TableFactory
import org.springframework.stereotype.Component
import java.util.ArrayList
import java.util.HashMap
import java.util.UUID

@Component
class DefaultTableFactory : TableFactory {

    override fun createNew(command: CreateTableCommand): Table {
        val numberOfPlayersPerTable = command.numberOfPlayersPerTable
        val seatMap = HashMap<Int, UUID?>()
        for (i in 0 until numberOfPlayersPerTable) {
            seatMap[i] = null
        }
        val playerIds = command.playerIds
        if (playerIds.size < 2) {
            throw FlexPokerException("must have at least two players")
        }
        if (playerIds.size > seatMap.size) {
            throw FlexPokerException("player list can't be larger than seatMap")
        }

        // TODO: randomize the seat placement
        val playerIdsList = ArrayList(playerIds)
        for (i in playerIds.indices) {
            seatMap[Integer.valueOf(i)] = playerIdsList[i]
        }

        // TODO: add starting chips as a parameter, probably once blind scheduling gets introduced
        return createWithGivenInfo(false, command.tableId, command.gameId, seatMap, 1500)
    }

    override fun createFrom(events: List<TableEvent>): Table {
        val tableCreatedEvent = events[0] as TableCreatedEvent
        val table = createWithGivenInfo(
            true,
            tableCreatedEvent.aggregateId,
            tableCreatedEvent.gameId,
            tableCreatedEvent.seatPositionToPlayerMap,
            tableCreatedEvent.startingNumberOfChips
        )
        table.applyAllHistoricalEvents(events)
        return table
    }

    private fun createWithGivenInfo(creatingFromEvents: Boolean, aggregateId: UUID, gameId: UUID,
                                    seatMap: Map<Int, UUID?>, startingNumberOfChips: Int): Table {
        return Table(creatingFromEvents, aggregateId, gameId, seatMap, startingNumberOfChips)
    }
}