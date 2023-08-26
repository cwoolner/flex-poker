package com.flexpoker.game.query.repository.impl

import com.flexpoker.game.query.repository.GamePlayerRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.util.UUID

fun sharedTestAddPlayerToGame(repository: GamePlayerRepository) {
    val playerId = UUID.randomUUID()
    val gameId = UUID.randomUUID()
    repository.addPlayerToGame(playerId, gameId)

    val playerIds = repository.fetchAllPlayerIdsForGame(gameId)
    assertEquals(1, playerIds.size)
    assertTrue(playerIds.contains(playerId))
}

fun sharedTestAddPlayerToGameDuplicate(repository: GamePlayerRepository) {
    val playerId = UUID.randomUUID()
    val gameId = UUID.randomUUID()
    repository.addPlayerToGame(playerId, gameId)
    repository.addPlayerToGame(playerId, gameId)

    val playerIds = repository.fetchAllPlayerIdsForGame(gameId)
    assertEquals(1, playerIds.size)
    assertTrue(playerIds.contains(playerId))
}

fun sharedTestFetchAllPlayerIdsForGameNoneAdded(repository: GamePlayerRepository) {
    val gameId = UUID.randomUUID()
    val playerIds = repository.fetchAllPlayerIdsForGame(gameId)
    assertEquals(0, playerIds.size)
}
