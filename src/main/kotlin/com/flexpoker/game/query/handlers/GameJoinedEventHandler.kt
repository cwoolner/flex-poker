package com.flexpoker.game.query.handlers

import com.flexpoker.chat.service.ChatService
import com.flexpoker.framework.event.EventHandler
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher
import com.flexpoker.game.command.events.GameJoinedEvent
import com.flexpoker.game.query.repository.GameListRepository
import com.flexpoker.game.query.repository.GamePlayerRepository
import com.flexpoker.game.query.repository.OpenGameForPlayerRepository
import com.flexpoker.login.repository.LoginRepository
import com.flexpoker.pushnotifications.GameListUpdatedPushNotification
import com.flexpoker.pushnotifications.OpenGamesForPlayerUpdatedPushNotification
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class GameJoinedEventHandler @Inject constructor(
    private val gameListRepository: GameListRepository,
    private val pushNotificationPublisher: PushNotificationPublisher,
    private val openGameForUserRepository: OpenGameForPlayerRepository,
    private val gamePlayerRepository: GamePlayerRepository,
    private val loginRepository: LoginRepository,
    private val chatService: ChatService
) : EventHandler<GameJoinedEvent> {

    override fun handle(event: GameJoinedEvent) {
        handleGamePlayerRepository(event)
        handleOpenGameRepository(event)
        handleGameListRepository(event)
        handlePushNotifications(event)
        handleChat(event)
    }

    private fun handleGameListRepository(event: GameJoinedEvent) {
        gameListRepository.incrementRegisteredPlayers(event.aggregateId)
    }

    private fun handleGamePlayerRepository(event: GameJoinedEvent) {
        gamePlayerRepository.addPlayerToGame(event.playerId, event.aggregateId)
    }

    private fun handleOpenGameRepository(event: GameJoinedEvent) {
        val gameName = gameListRepository.fetchGameName(event.aggregateId)
        openGameForUserRepository.addOpenGameForUser(event.playerId, event.aggregateId, gameName)
    }

    private fun handlePushNotifications(event: GameJoinedEvent) {
        pushNotificationPublisher.publish(OpenGamesForPlayerUpdatedPushNotification(event.playerId))
        pushNotificationPublisher.publish(GameListUpdatedPushNotification)
    }

    private fun handleChat(event: GameJoinedEvent) {
        val username = loginRepository.fetchUsernameByAggregateId(event.playerId)
        val message = "$username has joined the game"
        chatService.saveAndPushSystemGameChatMessage(event.aggregateId, message)
    }
}