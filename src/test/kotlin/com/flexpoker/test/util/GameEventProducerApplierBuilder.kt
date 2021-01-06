package com.flexpoker.test.util

import com.flexpoker.game.command.aggregate.GameState
import com.flexpoker.game.command.aggregate.applyEvent
import com.flexpoker.game.command.aggregate.applyEvents
import com.flexpoker.game.command.events.GameEvent
import org.junit.jupiter.api.Assertions.assertThrows

class GameEventProducerApplierBuilder {

    private var initState: GameState? = null
    private val funsToRun = mutableListOf<(GameState) -> List<GameEvent>>()
    private var exceptionClass: Class<out Exception>? = null
    private var exceptionFunToRun: ((GameState) -> List<GameEvent>)? = null

    fun initState(state: GameState) = apply { this.initState = state }

    fun initState(event: GameEvent) = apply { this.initState = applyEvent(null, event) }

    fun andRun(lambda: (GameState) -> List<GameEvent>) = apply { this.funsToRun.add(lambda) }

    fun andRunThrows(exceptionClass: Class<out Exception>, lambda: (GameState) -> List<GameEvent>) = apply {
        require(this.exceptionFunToRun == null) { "andRunThrows() should only be called once" }
        this.exceptionClass = exceptionClass
        this.exceptionFunToRun = lambda
    }

    fun run(): Pair<GameState, List<GameEvent>> {
        requireNotNull(initState) { "must call initState() before run()" }
        val runPairs = createStateEventPairs()
        return if (exceptionClass != null) {
            assertThrows(exceptionClass) { exceptionFunToRun!!(runPairs.first) }
            runPairs
        } else runPairs
    }

    private fun createStateEventPairs(): Pair<GameState, List<GameEvent>> {
        return funsToRun.fold(Pair(initState!!, emptyList()), { (accState, accEvents), funToRun ->
            val newEvents = funToRun(accState)
            Pair(
                applyEvents(accState, newEvents),
                accEvents + newEvents
            )
        })
    }

}