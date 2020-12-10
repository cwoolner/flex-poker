package com.flexpoker.pushnotifications

import com.flexpoker.framework.pushnotifier.BasePushNotification
import com.flexpoker.framework.pushnotifier.PushNotificationType
import com.flexpoker.table.command.PocketCards
import java.util.UUID

data class ChatSentPushNotification(val id: UUID, val gameId: UUID?, val tableId: UUID?,
                                    val message: String, val senderUsername: String?, val systemMessage: Boolean,
                                    val destination: String)
    : BasePushNotification(PushNotificationType.ChatSent)

object GameListUpdatedPushNotification : BasePushNotification(PushNotificationType.GameListUpdated)

data class OpenGamesForPlayerUpdatedPushNotification(val playerId: UUID)
    : BasePushNotification(PushNotificationType.OpenGamesForPlayerUpdated)

data class OpenTableForUserPushNotification(val gameId: UUID, val tableId: UUID, val playerId: UUID)
    : BasePushNotification(PushNotificationType.OpenTableForUser)

data class SendUserPocketCardsPushNotification(val playerId: UUID, val handId: UUID, val pocketCards: PocketCards)
    : BasePushNotification(PushNotificationType.SendUserPocketCards)

data class TableUpdatedPushNotification(val gameId: UUID, val tableId: UUID)
    : BasePushNotification(PushNotificationType.TableUpdated)

data class TickActionOnTimerPushNotification(val gameId: UUID, val tableId: UUID, val number: Int)
    : BasePushNotification(PushNotificationType.TickActionOnTimer)