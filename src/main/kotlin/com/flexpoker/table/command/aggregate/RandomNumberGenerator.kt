package com.flexpoker.table.command.aggregate

import java.util.UUID
import kotlin.random.Random

interface RandomNumberGenerator {
    fun int(exclusiveMax: Int): Int
    fun int(salt: UUID, exclusiveMax: Int): Int
}

class DefaultRandomNumberGenerator : RandomNumberGenerator {
    override fun int(exclusiveMax: Int): Int {
        return Random.nextInt(exclusiveMax)
    }

    override fun int(salt: UUID, exclusiveMax: Int): Int {
        return Random(salt.leastSignificantBits).nextInt(exclusiveMax)
    }
}