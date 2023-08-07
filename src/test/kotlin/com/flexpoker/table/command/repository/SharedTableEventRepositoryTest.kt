package com.flexpoker.table.command.repository

import org.junit.jupiter.api.Assertions

fun sharedTestSetEventVersionsAndSaveEmptyList(repository: TableEventRepository) {
    val eventsWithVersions = repository.setEventVersionsAndSave(0, emptyList())
    Assertions.assertTrue(eventsWithVersions.isEmpty())
}
