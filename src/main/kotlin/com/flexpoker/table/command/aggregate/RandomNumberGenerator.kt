package com.flexpoker.table.command.aggregate

import java.util.UUID
import kotlin.math.absoluteValue
import kotlin.random.Random

interface RandomNumberGenerator {
    fun int(exclusiveMax: Int): Int
    fun pseudoRandomIntBasedOnUUID(uuid: UUID, exclusiveMax: Int): Int
}

class DefaultRandomNumberGenerator : RandomNumberGenerator {
    override fun int(exclusiveMax: Int): Int {
        return Random.nextInt(exclusiveMax)
    }

    override fun pseudoRandomIntBasedOnUUID(uuid: UUID, exclusiveMax: Int): Int {
        return uuid.leastSignificantBits.absoluteValue.toInt() % exclusiveMax
    }
}