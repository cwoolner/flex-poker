package com.flexpoker.table.query.handlers

import com.flexpoker.chat.service.ChatService
import com.flexpoker.framework.event.EventHandler
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher
import com.flexpoker.login.repository.LoginRepository
import com.flexpoker.pushnotifications.TableUpdatedPushNotification
import com.flexpoker.table.command.events.PlayerFoldedEvent
import com.flexpoker.table.query.repository.TableRepository
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class PlayerFoldedEventHandler @Inject constructor(
    private val loginRepository: LoginRepository,
    private val tableRepository: TableRepository,
    private val pushNotificationPublisher: PushNotificationPublisher,
    private val chatService: ChatService
) : EventHandler<PlayerFoldedEvent> {

    override fun handle(event: PlayerFoldedEvent) {
        handleUpdatingTable(event)
        handlePushNotifications(event)
        handleChat(event)
    }

    private fun handleUpdatingTable(event: PlayerFoldedEvent) {
        val tableDTO = tableRepository.fetchById(event.aggregateId)
        val username = loginRepository.fetchUsernameByAggregateId(event.playerId)
        val updatedSeats = tableDTO.seats!!
            .map {
                if (it.name == username) {
                    it.copy(isStillInHand = false, raiseTo = 0, callAmount = 0, isActionOn = false)
                } else {
                    it
                }
            }
        val updatedPots = tableDTO.pots!!
            .map {
                val updatedPotSeats = it.seats.filter { seatName -> seatName != username }.toSet()
                it.copy(seats = updatedPotSeats)
            }.toSet()
        val updatedTable = tableDTO.copy(version = event.version, seats = updatedSeats, pots = updatedPots)
        tableRepository.save(updatedTable)
    }

    private fun handlePushNotifications(event: PlayerFoldedEvent) {
        val pushNotification = TableUpdatedPushNotification(event.gameId, event.aggregateId)
        pushNotificationPublisher.publish(pushNotification)
    }

    private fun handleChat(event: PlayerFoldedEvent) {
        val username = loginRepository.fetchUsernameByAggregateId(event.playerId)
        val message = "$username folds"
        chatService.saveAndPushSystemTableChatMessage(event.gameId, event.aggregateId, message)
    }

}