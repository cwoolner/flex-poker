package com.flexpoker.game.query.handlers

import com.flexpoker.chat.service.ChatService
import com.flexpoker.framework.event.EventHandler
import com.flexpoker.game.command.aggregate.applyEvents
import com.flexpoker.game.command.events.BlindsIncreasedEvent
import com.flexpoker.game.command.repository.GameEventRepository
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class BlindsIncreasedEventHandler @Inject constructor(
    private val chatService : ChatService,
    private val gameEventRepository: GameEventRepository
) : EventHandler<BlindsIncreasedEvent> {

    override fun handle(event: BlindsIncreasedEvent) {
        handleChat(event)
    }

    private fun handleChat(event: BlindsIncreasedEvent) {
        val gameEvents = gameEventRepository.fetchAll(event.gameId)
        val state = applyEvents(gameEvents)
        val tableIds = state.tableIdToPlayerIdsMap.keys
        val blinds = state.blindSchedule.currentBlinds()

        val message = "Blinds will increase next hand to ${blinds.smallBlind} / ${blinds.bigBlind}"
        chatService.saveAndPushSystemGameChatMessage(event.gameId, message)
        tableIds.forEach { chatService.saveAndPushSystemTableChatMessage(event.gameId, it, message) }
    }

}