package com.flexpoker.pushnotificationhandlers

import com.flexpoker.framework.pushnotifier.PushNotificationHandler
import com.flexpoker.pushnotifications.TableUpdatedPushNotification
import com.flexpoker.table.query.repository.TableRepository
import com.flexpoker.util.MessagingConstants
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class TableUpdatedPushNotificationHandler @Inject constructor(
    private val messagingTemplate: SimpMessageSendingOperations,
    private val tableRepository: TableRepository
) : PushNotificationHandler<TableUpdatedPushNotification> {

    @Async
    override fun handle(pushNotification: TableUpdatedPushNotification) {
        val tableDTO = tableRepository.fetchById(pushNotification.tableId)
        messagingTemplate.convertAndSend(
            String.format(MessagingConstants.TABLE_STATUS, pushNotification.gameId, pushNotification.tableId),
            tableDTO)
    }

}