package com.flexpoker.game.command.aggregate

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.game.command.events.PlayerMovedToNewTableEvent
import com.flexpoker.game.command.events.TablePausedForBalancingEvent
import com.flexpoker.game.command.events.TableRemovedEvent
import com.flexpoker.game.command.events.TableResumedAfterBalancingEvent
import org.pcollections.PMap
import org.pcollections.PSet
import java.util.Optional
import java.util.UUID

fun createSingleBalancingEvent(gameId: UUID, maxPlayersPerTable: Int, subjectTableId: UUID,
                               pausedTablesForBalancing: Set<UUID>, tableToPlayersMap: PMap<UUID, PSet<UUID>>,
                               playerToChipsAtTableMap: Map<UUID, Int>): Optional<GameEvent> {

    // make sure at least two players exists. we're in a bad state if not
    val totalNumberOfPlayers = getTotalNumberOfPlayers(tableToPlayersMap)
    if (totalNumberOfPlayers < 2) {
        throw FlexPokerException(
            "the game should be over since less than two players are left.  no balancing should be done"
        )
    }

    // remove any empty tables first
    val emptyTable = tableToPlayersMap.entries.firstOrNull { it.value.isEmpty() }
    if (emptyTable != null) {
        return Optional.of(TableRemovedEvent(gameId, emptyTable.key))
    }

    // check to see if the number of tables is greater than the number it
    // should have. if so, move all the players from this table to other
    // tables starting with the one with the lowest number
    if (tableToPlayersMap.size > getRequiredNumberOfTables(maxPlayersPerTable, totalNumberOfPlayers)) {
        return createEventToMoveUserFromSubjectTableToAnyMinTable(gameId,
            subjectTableId, tableToPlayersMap, playerToChipsAtTableMap)
    }
    if (isTableOutOfBalance(subjectTableId, tableToPlayersMap)) {
        val tableSize = tableToPlayersMap[subjectTableId]!!.size

        // in the min case, pause the table since it needs to get another player
        if (tableSize == tableToPlayersMap.values.map { it.size }.minOrNull()) {
            return Optional.of(TablePausedForBalancingEvent(gameId, subjectTableId))
        }

        // only move a player if the out of balance table has the max number
        // of players. an out-of-balance medium-sized table should not send
        // any players
        if (tableSize == tableToPlayersMap.values.map { it.size }.maxOrNull()) {
            return createEventToMoveUserFromSubjectTableToAnyMinTable(gameId,
                subjectTableId, tableToPlayersMap, playerToChipsAtTableMap)
        }
    }

    // re-examine the paused tables to see if they are still not balanced
    val pausedTablesThatShouldStayPaused = pausedTablesForBalancing
        .filter { isTableOutOfBalance(it, tableToPlayersMap) }
        .toSet()

    // resume the first table that is currently paused, but are now balanced
    val tableToResume = pausedTablesForBalancing.firstOrNull { !pausedTablesThatShouldStayPaused.contains(it) }

    return if (tableToResume == null) Optional.empty()
    else Optional.of(TableResumedAfterBalancingEvent(gameId, tableToResume))
}

private fun createEventToMoveUserFromSubjectTableToAnyMinTable(gameId: UUID, subjectTableId: UUID,
    tableToPlayersMap: Map<UUID, MutableSet<UUID>>, playerToChipsAtTableMap: Map<UUID, Int>
): Optional<GameEvent> {
    val playerToMove = tableToPlayersMap[subjectTableId]!!.first()
    val toTableId = findNonSubjectMinTableId(subjectTableId, tableToPlayersMap)
    val chips = playerToChipsAtTableMap[playerToMove]!!
    return Optional.of(PlayerMovedToNewTableEvent(gameId, subjectTableId, toTableId, playerToMove, chips))
}

private fun findNonSubjectMinTableId(subjectTableId: UUID, tableToPlayersMap: Map<UUID, MutableSet<UUID>>): UUID {
    return tableToPlayersMap.filter { it.key != subjectTableId }.minByOrNull { it.value.size }!!.key
}

private fun isTableOutOfBalance(tableId: UUID, tableToPlayersMap: Map<UUID, MutableSet<UUID>>): Boolean {
    val tableSize = tableToPlayersMap[tableId]!!.size
    return if (tableSize == 1) {
        true
    } else tableToPlayersMap.values.any { it.size >= tableSize + 2 || it.size <= tableSize - 2 }
}

private fun getTotalNumberOfPlayers(tableToPlayersMap: Map<UUID, MutableSet<UUID>>): Int {
    return tableToPlayersMap.values.sumBy { it.size }
}

private fun getRequiredNumberOfTables(maxPlayersPerTable: Int, totalNumberOfPlayers: Int): Int {
    var numberOfRequiredTables = totalNumberOfPlayers / maxPlayersPerTable
    if (totalNumberOfPlayers % maxPlayersPerTable != 0) {
        numberOfRequiredTables++
    }
    return numberOfRequiredTables
}
