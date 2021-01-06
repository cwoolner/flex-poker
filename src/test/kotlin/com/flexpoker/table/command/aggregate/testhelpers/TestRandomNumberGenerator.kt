package com.flexpoker.table.command.aggregate.testhelpers

import com.flexpoker.table.command.aggregate.DefaultRandomNumberGenerator
import com.flexpoker.table.command.aggregate.RandomNumberGenerator

class TestRandomNumberGenerator(private var counter: Int) : RandomNumberGenerator by DefaultRandomNumberGenerator() {
    override fun int(exclusiveMax: Int): Int {
        val current = minOf(counter, exclusiveMax.dec())
        counter = counter.inc()
        return current
    }
}