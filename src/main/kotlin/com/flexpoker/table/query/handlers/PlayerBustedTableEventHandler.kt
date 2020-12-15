package com.flexpoker.table.query.handlers

import com.flexpoker.chat.service.ChatService
import com.flexpoker.framework.event.EventHandler
import com.flexpoker.login.repository.LoginRepository
import com.flexpoker.table.command.events.PlayerBustedTableEvent
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class PlayerBustedTableEventHandler @Inject constructor(
    private val loginRepository: LoginRepository,
    private val chatService: ChatService
) : EventHandler<PlayerBustedTableEvent> {

    override fun handle(event: PlayerBustedTableEvent) {
        handleChat(event)
    }

    private fun handleChat(event: PlayerBustedTableEvent) {
        val username = loginRepository.fetchUsernameByAggregateId(event.playerId)
        val message = "$username is out"
        chatService.saveAndPushSystemTableChatMessage(event.gameId, event.aggregateId, message)
    }

}