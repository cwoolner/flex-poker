package com.flexpoker.test.util

import com.flexpoker.table.command.aggregate.TableState
import com.flexpoker.table.command.aggregate.applyEvent
import com.flexpoker.table.command.aggregate.applyEvents
import com.flexpoker.table.command.events.TableEvent
import org.junit.jupiter.api.Assertions.assertThrows

class TableEventProducerApplierBuilder {

    private var initState: TableState? = null
    private val funsToRun = mutableListOf<(TableState) -> List<TableEvent>>()
    private var exceptionClass: Class<out Exception>? = null
    private var exceptionFunToRun: ((TableState) -> List<TableEvent>)? = null

    fun initState(state: TableState) = apply { this.initState = state }

    fun initState(event: TableEvent) = apply { this.initState = applyEvent(null, event) }

    fun andRun(lambda: (TableState) -> List<TableEvent>) = apply { this.funsToRun.add(lambda) }

    fun andRunThrows(exceptionClass: Class<out Exception>, lambda: (TableState) -> List<TableEvent>) = apply {
        require(this.exceptionFunToRun == null) { "andRunThrows() should only be called once" }
        this.exceptionClass = exceptionClass
        this.exceptionFunToRun = lambda
    }

    fun run(): Pair<TableState, List<TableEvent>> {
        requireNotNull(initState) { "must call initState() before run()" }
        val runPairs = createStateEventPairs()
        return if (exceptionClass != null) {
            assertThrows(exceptionClass) { exceptionFunToRun!!(runPairs.first) }
            runPairs
        } else runPairs
    }

    private fun createStateEventPairs(): Pair<TableState, List<TableEvent>> {
        return funsToRun.fold(Pair(initState!!, emptyList()), { (accState, accEvents), funToRun ->
            val newEvents = funToRun(accState)
            Pair(
                applyEvents(accState, *newEvents.toTypedArray()),
                accEvents + newEvents
            )
        })
    }

}