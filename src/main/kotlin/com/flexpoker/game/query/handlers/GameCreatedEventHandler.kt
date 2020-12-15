package com.flexpoker.game.query.handlers

import com.flexpoker.framework.event.EventHandler
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher
import com.flexpoker.game.command.events.GameCreatedEvent
import com.flexpoker.game.query.dto.GameInListDTO
import com.flexpoker.game.query.dto.GameStage
import com.flexpoker.game.query.repository.GameListRepository
import com.flexpoker.login.repository.LoginRepository
import com.flexpoker.pushnotifications.GameListUpdatedPushNotification
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class GameCreatedEventHandler @Inject constructor(
    private val loginRepository: LoginRepository,
    private val gameListRepository: GameListRepository,
    private val pushNotificationPublisher: PushNotificationPublisher
) : EventHandler<GameCreatedEvent> {

    override fun handle(event: GameCreatedEvent) {
        handleGameListRepository(event)
        handlePushNotifications()
    }

    private fun handleGameListRepository(event: GameCreatedEvent) {
        val createdByUsername = loginRepository.fetchUsernameByAggregateId(event.createdByPlayerId)
        val gameInListDTO = GameInListDTO(
            event.aggregateId,
            event.gameName, GameStage.REGISTERING.toString(), 0,
            event.numberOfPlayers, event.numberOfPlayersPerTable,
            event.numberOfMinutesBetweenBlindLevels,
            event.numberOfSecondsForActionOnTimer,
            createdByUsername, event.time.toString()
        )
        gameListRepository.saveNew(gameInListDTO)
    }

    private fun handlePushNotifications() {
        pushNotificationPublisher.publish(GameListUpdatedPushNotification)
    }
}