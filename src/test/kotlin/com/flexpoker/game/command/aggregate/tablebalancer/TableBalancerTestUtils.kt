package com.flexpoker.game.command.aggregate.tablebalancer

import java.util.HashMap
import java.util.UUID

object TableBalancerTestUtils {

    /**
     * Helper method to create a set of table to player maps. The subject
     * table/number of players is treated specially in order to keep some
     * control over the tests.
     */
    fun createTableToPlayersMap(subjectTableId: UUID, numberOfSubjectTablePlayers: Int,
                                vararg numberOfPlayersAtOtherTables: Int): Map<UUID, Set<UUID>> {
        val tableToPlayersMap = HashMap<UUID, Set<UUID>>()
        tableToPlayersMap[subjectTableId] = createRandomSetOfPlayerIds(numberOfSubjectTablePlayers)
        for (element in numberOfPlayersAtOtherTables) {
            tableToPlayersMap[UUID.randomUUID()] = createRandomSetOfPlayerIds(element)
        }
        return tableToPlayersMap
    }

    private fun createRandomSetOfPlayerIds(numberOfPlayers: Int): Set<UUID> {
        return (0 until numberOfPlayers).map { UUID.randomUUID() }.toSet()
    }

    fun createDefaultChipMapForSubjectTable(subjectTableId: UUID, tableToPlayersMap: Map<UUID, Set<UUID>>): Map<UUID, Int> {
        return tableToPlayersMap[subjectTableId]!!.associateWith { 100 }
    }

}