package com.flexpoker.pushnotificationhandlers

import com.flexpoker.framework.pushnotifier.PushNotificationHandler
import com.flexpoker.login.repository.LoginRepository
import com.flexpoker.pushnotifications.OpenTableForUserPushNotification
import com.flexpoker.table.query.dto.OpenTableForUserDTO
import com.flexpoker.util.MessagingConstants
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class OpenTableForUserPushNotificationHandler @Inject constructor(
    private val loginRepository: LoginRepository,
    private val messagingTemplate: SimpMessageSendingOperations
) : PushNotificationHandler<OpenTableForUserPushNotification> {

    @Async
    override fun handle(pushNotification: OpenTableForUserPushNotification) {
        val username = loginRepository.fetchUsernameByAggregateId(pushNotification.playerId)
        val dto = OpenTableForUserDTO(pushNotification.gameId, pushNotification.tableId)
        messagingTemplate.convertAndSendToUser(username, MessagingConstants.OPEN_TABLE_FOR_USER, dto)
    }

}