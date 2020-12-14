package com.flexpoker.pushnotifications

import com.flexpoker.table.command.PocketCards
import java.util.UUID

sealed class PushNotification

data class ChatSentPushNotification(val id: UUID, val gameId: UUID?, val tableId: UUID?,
                                    val message: String, val senderUsername: String?, val systemMessage: Boolean,
                                    val destination: String) : PushNotification()

object GameListUpdatedPushNotification : PushNotification()

data class OpenGamesForPlayerUpdatedPushNotification(val playerId: UUID) : PushNotification()

data class OpenTableForUserPushNotification(val gameId: UUID, val tableId: UUID, val playerId: UUID) : PushNotification()

data class SendUserPocketCardsPushNotification(val playerId: UUID, val handId: UUID, val pocketCards: PocketCards) : PushNotification()

data class TableUpdatedPushNotification(val gameId: UUID, val tableId: UUID) : PushNotification()

data class TickActionOnTimerPushNotification(val gameId: UUID, val tableId: UUID, val number: Int) : PushNotification()