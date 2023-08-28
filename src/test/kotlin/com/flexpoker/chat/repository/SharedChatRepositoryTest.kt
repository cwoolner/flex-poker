package com.flexpoker.chat.repository

import com.flexpoker.web.dto.OutgoingChatMessageDTO
import org.junit.jupiter.api.Assertions.assertEquals
import java.util.UUID

fun sharedTestSaveChatMessage(repository: ChatRepository) {
    val id = UUID.randomUUID()
    val lobbyChatMessage = OutgoingChatMessageDTO(
        id, null, null, "test", "testuser", false
    )
    repository.saveChatMessage(lobbyChatMessage)
    val lobbyChatMessages = repository.fetchAllLobbyChatMessages()
    assertEquals(1, lobbyChatMessages.size)
    assertEquals(id, lobbyChatMessages[0].id)
}

fun sharedTestFetchAllTypesEmpty(repository: ChatRepository) {
    assertEquals(0, repository.fetchAllLobbyChatMessages().size)
    assertEquals(0, repository.fetchAllGameChatMessages(UUID.randomUUID()).size)
    assertEquals(0, repository.fetchAllTableChatMessages(UUID.randomUUID()).size)
}

fun sharedTestFetchAllTypes(repository: ChatRepository) {
    val lobbyChatMessageId = UUID.randomUUID()
    val lobbyChatMessage = OutgoingChatMessageDTO(
        lobbyChatMessageId, null, null, "test lobby", "testuser", false
    )

    val gameChatMessageId = UUID.randomUUID()
    val gameId = UUID.randomUUID()
    val gameChatMessage = OutgoingChatMessageDTO(
        gameChatMessageId, gameId, null, "test game", "testuser", false
    )

    val tableChatMessageId = UUID.randomUUID()
    val tableId = UUID.randomUUID()
    val tableChatMessage = OutgoingChatMessageDTO(
        tableChatMessageId, gameId, tableId, "test table", "testuser", false
    )

    repository.saveChatMessage(lobbyChatMessage)
    repository.saveChatMessage(gameChatMessage)
    repository.saveChatMessage(tableChatMessage)
    val lobbyChats = repository.fetchAllLobbyChatMessages()
    val gameChats = repository.fetchAllGameChatMessages(gameId)
    val tableChats = repository.fetchAllTableChatMessages(tableId)

    assertEquals(1, lobbyChats.size)
    assertEquals(lobbyChatMessageId, lobbyChats[0].id)
    assertEquals(1, gameChats.size)
    assertEquals(gameChatMessageId, gameChats[0].id)
    assertEquals(1, tableChats.size)
    assertEquals(tableChatMessageId, tableChats[0].id)
}
