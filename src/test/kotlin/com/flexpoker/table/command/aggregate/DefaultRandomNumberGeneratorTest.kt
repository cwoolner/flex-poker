package com.flexpoker.table.command.aggregate

import com.flexpoker.test.util.UnitTestClass
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.UUID

@UnitTestClass
class DefaultRandomNumberGeneratorTest {

    @Test
    fun testInt() {
        val actual = DefaultRandomNumberGenerator().int(1)
        assertEquals(0, actual)
    }

    @Test
    fun testPseudoRandomIntBasedOnUUID() {
        val uuid = UUID.fromString("5af9acbf-9ae9-45f9-b0c8-0d13531ad6a7")
        val actual = DefaultRandomNumberGenerator().pseudoRandomIntBasedOnUUID(uuid, 5)
        assertEquals(3, actual)
    }

}