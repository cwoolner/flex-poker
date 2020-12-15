package com.flexpoker.table.query.handlers

import com.flexpoker.chat.service.ChatService
import com.flexpoker.framework.event.EventHandler
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher
import com.flexpoker.login.repository.LoginRepository
import com.flexpoker.pushnotifications.TableUpdatedPushNotification
import com.flexpoker.table.command.events.PlayerCheckedEvent
import com.flexpoker.table.query.repository.TableRepository
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class PlayerCheckedEventHandler @Inject constructor(
    private val loginRepository: LoginRepository,
    private val tableRepository: TableRepository,
    private val pushNotificationPublisher: PushNotificationPublisher,
    private val chatService: ChatService
) : EventHandler<PlayerCheckedEvent> {

    override fun handle(event: PlayerCheckedEvent) {
        handleUpdatingTable(event)
        handlePushNotifications(event)
        handleChat(event)
    }

    private fun handleUpdatingTable(event: PlayerCheckedEvent) {
        val tableDTO = tableRepository.fetchById(event.aggregateId)
        val username = loginRepository.fetchUsernameByAggregateId(event.playerId)
        val updatedSeats = tableDTO.seats!!
            .map {
                if (it.name == username) {
                    it.copy(raiseTo = 0, callAmount = 0, isActionOn = false)
                } else {
                    it
                }
            }
        val updatedTable = tableDTO.copy(version = event.version, seats = updatedSeats)
        tableRepository.save(updatedTable)
    }

    private fun handlePushNotifications(event: PlayerCheckedEvent) {
        val pushNotification = TableUpdatedPushNotification(event.gameId, event.aggregateId)
        pushNotificationPublisher.publish(pushNotification)
    }

    private fun handleChat(event: PlayerCheckedEvent) {
        val username = loginRepository.fetchUsernameByAggregateId(event.playerId)
        val message = "$username checks"
        chatService.saveAndPushSystemTableChatMessage(event.gameId, event.aggregateId, message)
    }

}