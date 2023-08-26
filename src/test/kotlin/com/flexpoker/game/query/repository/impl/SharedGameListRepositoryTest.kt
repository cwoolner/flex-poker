package com.flexpoker.game.query.repository.impl

import com.flexpoker.game.command.GameStage
import com.flexpoker.game.query.dto.GameInListDTO
import com.flexpoker.game.query.repository.GameListRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.util.UUID

fun defaultGameInListDTO(gameId: UUID): GameInListDTO {
    return GameInListDTO(gameId, "test", "REGISTERING", 0, 2,
        2, 2, 2, "player1", "2023-08-01")
}

fun sharedTestSaveNew(repository: GameListRepository) {
    val gameId = UUID.randomUUID()
    val gameInListDTO = defaultGameInListDTO(gameId)
    repository.saveNew(gameInListDTO)

    val gameInListDTOs = repository.fetchAll()
    assertEquals(gameInListDTO, gameInListDTOs[0])
}

fun sharedTestFetchAllEmpty(repository: GameListRepository) {
    val gameInListDTOs = repository.fetchAll()
    assertTrue(gameInListDTOs.isEmpty())
}

fun sharedTestFetchAllTwoSaved(repository: GameListRepository) {
    val gameId1 = UUID.randomUUID()
    val gameId2 = UUID.randomUUID()
    repository.saveNew(defaultGameInListDTO(gameId1))
    repository.saveNew(defaultGameInListDTO(gameId2))

    val gameInListDTOs = repository.fetchAll()
    assertEquals(2, gameInListDTOs.size)
    assertTrue(gameInListDTOs.map { it.id }.containsAll(listOf(gameId1, gameId2)))
}

fun sharedTestIncrementRegisteredPlayers(repository: GameListRepository) {
    val gameId1 = UUID.randomUUID()
    val gameId2 = UUID.randomUUID()
    repository.saveNew(defaultGameInListDTO(gameId1))
    repository.saveNew(defaultGameInListDTO(gameId2))
    repository.incrementRegisteredPlayers(gameId1)
    assertEquals(1, repository.fetchAll().single { it.id == gameId1 }.numberOfRegisteredPlayers)
    assertEquals(0, repository.fetchAll().single { it.id == gameId2 }.numberOfRegisteredPlayers)
    assertEquals(2, repository.fetchAll().size)
}

fun sharedTestFetchGameName(repository: GameListRepository) {
    val gameId = UUID.randomUUID()
    repository.saveNew(defaultGameInListDTO(gameId))
    assertEquals("test", repository.fetchGameName(gameId))
}

fun sharedTestChangeGameStage(repository: GameListRepository) {
    val gameId1 = UUID.randomUUID()
    val gameId2 = UUID.randomUUID()
    repository.saveNew(defaultGameInListDTO(gameId1))
    repository.saveNew(defaultGameInListDTO(gameId2))
    repository.changeGameStage(gameId1, GameStage.STARTING)
    assertEquals("STARTING", repository.fetchAll().single { it.id == gameId1 }.stage)
    assertEquals("REGISTERING", repository.fetchAll().single { it.id == gameId2 }.stage)
    assertEquals(2, repository.fetchAll().size)
}