package com.flexpoker.game.query.handlers

import com.flexpoker.chat.service.ChatService
import com.flexpoker.framework.event.EventHandler
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher
import com.flexpoker.game.command.events.GameMovedToStartingStageEvent
import com.flexpoker.game.query.dto.GameStage
import com.flexpoker.game.query.repository.GameListRepository
import com.flexpoker.game.query.repository.GamePlayerRepository
import com.flexpoker.game.query.repository.OpenGameForPlayerRepository
import com.flexpoker.pushnotifications.GameListUpdatedPushNotification
import com.flexpoker.pushnotifications.OpenGamesForPlayerUpdatedPushNotification
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.util.UUID
import javax.inject.Inject

@Component
class GameMovedToStartingStageEventHandler @Inject constructor(
    private val gameListRepository: GameListRepository,
    private val gamePlayerRepository: GamePlayerRepository,
    private val openGameForUserRepository: OpenGameForPlayerRepository,
    private val pushNotificationPublisher: PushNotificationPublisher,
    private val chatService: ChatService
) : EventHandler<GameMovedToStartingStageEvent> {

    @Async
    override fun handle(event: GameMovedToStartingStageEvent) {
        val playerIdsForGame = handleOpenGameRepository(event)
        handleGameListRepository(event)
        handlePushNotifications(playerIdsForGame)
        handleChat(event)
    }

    private fun handleOpenGameRepository(event: GameMovedToStartingStageEvent): Set<UUID> {
        val playerIdsForGame = gamePlayerRepository.fetchAllPlayerIdsForGame(event.aggregateId)
        playerIdsForGame.forEach {
            openGameForUserRepository.changeGameStage(it, event.aggregateId, GameStage.STARTING)
        }
        return playerIdsForGame
    }

    private fun handleGameListRepository(event: GameMovedToStartingStageEvent) {
        gameListRepository.changeGameStage(event.aggregateId, GameStage.STARTING)
    }

    private fun handlePushNotifications(playerIdsForGame: Set<UUID>) {
        playerIdsForGame.forEach {
            pushNotificationPublisher.publish(OpenGamesForPlayerUpdatedPushNotification(it))
        }
        pushNotificationPublisher.publish(GameListUpdatedPushNotification)
    }

    private fun handleChat(event: GameMovedToStartingStageEvent) {
        val message = "Game will be starting shortly"
        chatService.saveAndPushSystemGameChatMessage(event.aggregateId, message)
    }
}