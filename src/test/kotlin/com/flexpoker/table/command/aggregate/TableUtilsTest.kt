package com.flexpoker.table.command.aggregate

import com.flexpoker.table.command.aggregate.eventproducers.checkHandIsBeingPlayed
import com.flexpoker.table.command.aggregate.eventproducers.numberOfPlayersAtTable
import com.flexpoker.table.command.aggregate.testhelpers.createBasicTable
import com.flexpoker.test.util.UnitTestClass
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.pcollections.HashTreePMap
import java.util.UUID

@UnitTestClass
class CheckHandIsBeingPlayed {

    @Test
    fun `hand is in play`() {
        val state = createBasicTable(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()).copy(
            currentHand = genericHandState()
        )
        checkHandIsBeingPlayed(state)
    }

    @Test
    fun `hand is not in play`() {
        val state = createBasicTable(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()).copy(
            currentHand = null
        )
        assertThrows(IllegalArgumentException::class.java) { checkHandIsBeingPlayed(state) }
    }

}

@UnitTestClass
class NumberOfPlayersAtTable {

    @Test
    fun `number of players is correct`() {
        val state = createBasicTable(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()).copy(
            seatMap = HashTreePMap
                .singleton(0, UUID.randomUUID())
                .plus(4, null)
                .plus(7, UUID.randomUUID())
        )
        assertEquals(2, numberOfPlayersAtTable(state))
    }

}