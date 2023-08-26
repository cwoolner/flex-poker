package com.flexpoker.game.query.repository.impl

import com.flexpoker.game.command.GameStage
import com.flexpoker.game.query.repository.OpenGameForPlayerRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import java.util.UUID

fun sharedTestFetchAllOpenGamesForPlayerNoGames(repository: OpenGameForPlayerRepository) {
    val playerId = UUID.randomUUID()
    val openGamesForPlayer = repository.fetchAllOpenGamesForPlayer(playerId)
    assertEquals(0, openGamesForPlayer.size)
}

fun sharedTestFetchAllOpenGamesOrdered(repository: OpenGameForPlayerRepository) {
    val playerId = UUID.randomUUID()
    val gameId1 = UUID.randomUUID()
    val gameId2 = UUID.randomUUID()
    repository.addOpenGameForUser(playerId, gameId1, "test1")
    repository.addOpenGameForUser(playerId, gameId2, "test2")

    val openGamesForPlayer = repository.fetchAllOpenGamesForPlayer(playerId)
    assertEquals(2, openGamesForPlayer.size)
    assertEquals(0, openGamesForPlayer[0].ordinal)
    assertEquals(1, openGamesForPlayer[1].ordinal)
}

fun sharedTestDeleteOpenGameForPlayer(repository: OpenGameForPlayerRepository) {
    val playerId = UUID.randomUUID()
    val gameId1 = UUID.randomUUID()
    val gameId2 = UUID.randomUUID()
    val gameId3 = UUID.randomUUID()
    repository.addOpenGameForUser(playerId, gameId1, "test1")
    repository.addOpenGameForUser(playerId, gameId2, "test2")
    repository.addOpenGameForUser(playerId, gameId3, "test3")

    repository.deleteOpenGameForPlayer(playerId, gameId2)

    val openGamesForPlayer = repository.fetchAllOpenGamesForPlayer(playerId)
    assertEquals(2, openGamesForPlayer.size)
    assertEquals(gameId1, openGamesForPlayer[0].gameId)
    assertEquals(0, openGamesForPlayer[0].ordinal)
    assertEquals(gameId3, openGamesForPlayer[1].gameId)
    assertEquals(1, openGamesForPlayer[1].ordinal)
}

fun sharedTestAddOpenGameForUser(repository: OpenGameForPlayerRepository) {
    val playerId = UUID.randomUUID()
    val gameId = UUID.randomUUID()

    repository.addOpenGameForUser(playerId, gameId, "test")

    val openGamesForPlayer = repository.fetchAllOpenGamesForPlayer(playerId)
    assertEquals(1, openGamesForPlayer.size)
    assertEquals(gameId, openGamesForPlayer[0].gameId)
    assertEquals(0, openGamesForPlayer[0].ordinal)
}

fun sharedTestChangeGameStage(repository: OpenGameForPlayerRepository) {
    val playerId = UUID.randomUUID()
    val gameId = UUID.randomUUID()
    repository.addOpenGameForUser(playerId, gameId, "test")

    val openGamesForPlayerBefore = repository.fetchAllOpenGamesForPlayer(playerId)
    assertEquals(GameStage.REGISTERING, openGamesForPlayerBefore[0].gameStage)

    repository.changeGameStage(playerId, gameId, GameStage.FINISHED)

    val openGamesForPlayerAfter = repository.fetchAllOpenGamesForPlayer(playerId)
    assertEquals(GameStage.FINISHED, openGamesForPlayerAfter[0].gameStage)
}

fun sharedTestAssignTableToOpenGame(repository: OpenGameForPlayerRepository) {
    val playerId = UUID.randomUUID()
    val gameId = UUID.randomUUID()
    val tableId = UUID.randomUUID()
    repository.addOpenGameForUser(playerId, gameId, "test")

    val openGamesForPlayerBefore = repository.fetchAllOpenGamesForPlayer(playerId)
    assertNull(openGamesForPlayerBefore[0].myTableId)

    repository.assignTableToOpenGame(playerId, gameId, tableId)

    val openGamesForPlayerAfter = repository.fetchAllOpenGamesForPlayer(playerId)
    assertEquals(tableId, openGamesForPlayerAfter[0].myTableId)
}
