package com.flexpoker.chat.service

import java.util.UUID

interface ChatService {
    fun saveAndPushSystemLobbyChatMessage(message: String)
    fun saveAndPushUserLobbyChatMessage(message: String, username: String?)
    fun saveAndPushSystemGameChatMessage(gameId: UUID?, message: String)
    fun saveAndPushUserGameChatMessage(gameId: UUID?, message: String, username: String?)
    fun saveAndPushSystemTableChatMessage(gameId: UUID?, tableId: UUID?, message: String)
    fun saveAndPushUserTableChatMessage(gameId: UUID?, tableId: UUID?, message: String, username: String?)
}