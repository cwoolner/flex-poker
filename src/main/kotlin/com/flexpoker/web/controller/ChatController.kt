package com.flexpoker.web.controller

import com.flexpoker.chat.repository.ChatRepository
import com.flexpoker.chat.service.ChatService
import com.flexpoker.web.dto.IncomingChatMessageDTO
import com.flexpoker.web.dto.OutgoingChatMessageDTO
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.annotation.SubscribeMapping
import org.springframework.stereotype.Controller
import java.security.Principal
import java.util.UUID
import javax.inject.Inject

@Controller
class ChatController @Inject constructor(
    private val chatRepository: ChatRepository,
    private val chatService: ChatService) {

    @MessageMapping("/app/sendchatmessage")
    fun sendChatMessage(chatMessage: IncomingChatMessageDTO, principal: Principal) {
        val gameId = chatMessage.gameId
        val tableId = chatMessage.tableId
        val message = chatMessage.message
        val username = principal.name
        if (gameId != null && tableId != null) {
            chatService.saveAndPushUserTableChatMessage(gameId, tableId, message, username)
        } else if (gameId != null) {
            chatService.saveAndPushUserGameChatMessage(gameId, message, username)
        } else {
            chatService.saveAndPushUserLobbyChatMessage(message, username)
        }
    }

    @SubscribeMapping("/topic/chat/lobby")
    fun fetchAllLobbyChatMessages(): List<OutgoingChatMessageDTO> {
        return chatRepository.fetchAllLobbyChatMessages()
    }

    @SubscribeMapping("/topic/chat/game/{gameId}")
    fun fetchAllGameChatMessages(@DestinationVariable gameId: UUID): List<OutgoingChatMessageDTO> {
        return chatRepository.fetchAllGameChatMessages(gameId)
    }

    @SubscribeMapping("/topic/chat/game/{gameId}/table/{tableId}")
    fun fetchAllTableChatMessages(@DestinationVariable gameId: UUID,
                                  @DestinationVariable tableId: UUID): List<OutgoingChatMessageDTO> {
        return chatRepository.fetchAllTableChatMessages(tableId)
    }

}