package com.flexpoker.table.query.repository.impl

import com.flexpoker.table.query.dto.TableDTO
import com.flexpoker.table.query.repository.TableRepository
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import java.util.UUID
import java.util.concurrent.CopyOnWriteArrayList
import java.util.function.Consumer

fun sharedTestFetchNonExistentTable(repository: TableRepository) {
    assertThrows(NullPointerException::class.java) { repository.fetchById(UUID.randomUUID()) }
}

fun sharedTestFetchExistingTable(repository: TableRepository) {
    val tableId = UUID.randomUUID()
    repository.save(TableDTO(tableId, 1, null, 0, null, null, 0, UUID.randomUUID()))
    assertNotNull(repository.fetchById(tableId))
}

fun sharedTestSaveIncrementingVersions(repository: TableRepository) {
    val tableId = UUID.randomUUID()
    repository.save(TableDTO(tableId, 1, null, 0, null, null, 0, UUID.randomUUID()))
    repository.save(TableDTO(tableId, 2, null, 0, null, null, 0, UUID.randomUUID()))
    assertEquals(2, repository.fetchById(tableId).version)
}

fun sharedTestSaveDecrementingVersions(repository: TableRepository) {
    val tableId = UUID.randomUUID()
    repository.save(TableDTO(tableId, 2, null, 0, null, null, 0, UUID.randomUUID()))
    repository.save(TableDTO(tableId, 1, null, 0, null, null, 0, UUID.randomUUID()))
    assertEquals(2, repository.fetchById(tableId).version)
}

fun sharedTestSaveDuplicateVersionsDoNotOverwrite(repository: TableRepository) {
    val tableId = UUID.randomUUID()
    repository.save(TableDTO(tableId, 2, null, 10, null, null, 0, UUID.randomUUID()))
    repository.save(TableDTO(tableId, 2, null, 20, null, null, 0, UUID.randomUUID()))
    assertEquals(10, repository.fetchById(tableId).totalPot)
}

fun sharedTestSaveMultithreadVersionsWithTwoDifferentTables(repository: TableRepository) {
    val table1Id = UUID.randomUUID()
    val table2Id = UUID.randomUUID()
    val saveThreads = (1 until 11)
        .map {
            val tableId = if (it % 2 == 0) table2Id else table1Id
            val version = if (it % 2 == 0) it / 2 else it / 2 + 1
            TableDTO(tableId, version, null, 0, null, null, 0, UUID.randomUUID())
        }
        .map { Thread { repository.save(it) } }
        .toSet()
    saveThreads.forEach { it.start() }
    // using a foreach cause of the checked exception
    for (thread in saveThreads) {
        thread.join()
    }
    assertEquals(5, repository.fetchById(table1Id).version)
    assertEquals(5, repository.fetchById(table2Id).version)
}

fun sharedTestFetchMultithreaded(repository: TableRepository) {
    val tableId = UUID.randomUUID()
    repository.save(TableDTO(tableId, 1, null, 0, null, null, 0, UUID.randomUUID()))
    val testAssertsPassed1 = CopyOnWriteArrayList<Any>()
    val readThreads1 = (0 until 5)
        .map {
            Thread {
                try {
                    assertEquals(1, repository.fetchById(tableId).version)
                    testAssertsPassed1.add(true)
                } catch (e: AssertionError) {
                    testAssertsPassed1.add(false)
                }
            }
        }
        .toSet()
    readThreads1.forEach { it.start() }
    // using a foreach cause of the checked exception
    for (thread in readThreads1) {
        thread.join()
    }
    assertArrayEquals(arrayOf(true, true, true, true, true), testAssertsPassed1.toTypedArray())
    val save2Thread = Thread { repository.save(
        TableDTO(tableId, 2, null, 0,
        null, null, 0, UUID.randomUUID())
    ) }
    save2Thread.start()
    val testAssertsPassed2 = CopyOnWriteArrayList<Any>()
    val readThreads2 = (0 until 5)
        .map {
            Thread {
                try {
                    assertEquals(2, repository.fetchById(tableId).version)
                    testAssertsPassed2.add(true)
                } catch (e: AssertionError) {
                    testAssertsPassed2.add(false)
                }
            }
        }
        .toSet()
    save2Thread.join()
    readThreads2.forEach(Consumer { x: Thread -> x.start() })

    // using a foreach cause of the checked exception
    for (thread in readThreads2) {
        thread.join()
    }
    assertArrayEquals(arrayOf(true, true, true, true, true), testAssertsPassed2.toTypedArray())
}
