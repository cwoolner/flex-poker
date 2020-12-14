package com.flexpoker.pushnotificationhandlers

import com.flexpoker.framework.pushnotifier.PushNotificationHandler
import com.flexpoker.login.repository.LoginRepository
import com.flexpoker.pushnotifications.SendUserPocketCardsPushNotification
import com.flexpoker.table.query.dto.PocketCardsDTO
import com.flexpoker.util.MessagingConstants
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class SendUserPocketCardsPushNotificationHandler @Inject constructor(
    private val loginRepository: LoginRepository,
    private val messagingTemplate: SimpMessageSendingOperations
) : PushNotificationHandler<SendUserPocketCardsPushNotification> {

    @Async
    override fun handle(pushNotification: SendUserPocketCardsPushNotification) {
        val username = loginRepository.fetchUsernameByAggregateId(pushNotification.playerId)
        val pocketCardsDTO = PocketCardsDTO(
            pushNotification.handId,
            pushNotification.pocketCards.card1.id,
            pushNotification.pocketCards.card2.id
        )
        messagingTemplate.convertAndSendToUser(username, MessagingConstants.POCKET_CARDS, pocketCardsDTO)
    }

}