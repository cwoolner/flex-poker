package com.flexpoker.test.util

import com.flexpoker.framework.command.DomainState
import com.flexpoker.framework.event.Event
import com.flexpoker.game.command.aggregate.GameState
import com.flexpoker.game.command.aggregate.applyEvent as gameApplyEvent
import com.flexpoker.game.command.aggregate.applyEvents as gameApplyEvents
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.table.command.aggregate.TableState
import com.flexpoker.table.command.aggregate.applyEvent as tableApplyEvent
import com.flexpoker.table.command.aggregate.applyEvents as tableApplyEvents
import com.flexpoker.table.command.events.TableEvent
import org.junit.jupiter.api.Assertions.assertThrows

class EventProducerApplierBuilder<S: DomainState, E: Event> {

    private var initState: S? = null
    private val funsToRun = mutableListOf<(S) -> List<E>>()
    private var exceptionClass: Class<out Exception>? = null
    private var exceptionFunToRun: ((S) -> List<E>)? = null

    fun initState(state: S) = apply { this.initState = state }

    fun initState(event: E) = apply {
        when (event) {
            is GameEvent -> this.initState = gameApplyEvent(null, event) as S?
            is TableEvent -> this.initState = tableApplyEvent(null, event) as S?
        }
    }

    fun andRun(lambda: (S) -> List<E>) = apply { this.funsToRun.add(lambda) }

    fun andRunThrows(exceptionClass: Class<out Exception>, lambda: (S) -> List<E>) = apply {
        require(this.exceptionFunToRun == null) { "andRunThrows() should only be called once" }
        this.exceptionClass = exceptionClass
        this.exceptionFunToRun = lambda
    }

    fun run(): Pair<S, List<E>> {
        requireNotNull(initState) { "must call initState() before run()" }
        val runPairs = createStateEventPairs()
        return if (exceptionClass != null) {
            assertThrows(exceptionClass) { exceptionFunToRun!!(runPairs.first) }
            runPairs
        } else runPairs
    }

    private fun createStateEventPairs(): Pair<S, List<E>> {
        return when (initState) {
            is GameState ->
                funsToRun.fold(Pair(initState!!, emptyList())) { (accState, accEvents), funToRun ->
                    val newEvents = funToRun(accState)
                    Pair(gameApplyEvents(accState as GameState, newEvents as List<GameEvent>) as S, accEvents + newEvents)
                }
            is TableState ->
                funsToRun.fold(Pair(initState!!, emptyList())) { (accState, accEvents), funToRun ->
                    val newEvents = funToRun(accState)
                    Pair(tableApplyEvents(accState as TableState, newEvents as List<TableEvent>) as S, accEvents + newEvents)
                }
            else -> throw IllegalArgumentException("must be gamestate or tablestate")
        }
    }

}