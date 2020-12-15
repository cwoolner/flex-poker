package com.flexpoker.game.query.handlers

import com.flexpoker.chat.service.ChatService
import com.flexpoker.framework.event.EventHandler
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher
import com.flexpoker.game.command.events.PlayerBustedGameEvent
import com.flexpoker.login.repository.LoginRepository
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class PlayerBustedGameEventHandler @Inject constructor(
    private val pushNotificationPublisher: PushNotificationPublisher,
    private val loginRepository: LoginRepository,
    private val chatService: ChatService
) : EventHandler<PlayerBustedGameEvent> {

    override fun handle(event: PlayerBustedGameEvent) {
        handleChat(event)
    }

    private fun handleChat(event: PlayerBustedGameEvent) {
        val username = loginRepository.fetchUsernameByAggregateId(event.playerId)
        val message = "$username is out"
        chatService.saveAndPushSystemGameChatMessage(event.aggregateId, message)
    }
}