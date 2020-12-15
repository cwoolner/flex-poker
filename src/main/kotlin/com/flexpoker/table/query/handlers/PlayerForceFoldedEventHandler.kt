package com.flexpoker.table.query.handlers

import com.flexpoker.chat.service.ChatService
import com.flexpoker.framework.event.EventHandler
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher
import com.flexpoker.login.repository.LoginRepository
import com.flexpoker.pushnotifications.TableUpdatedPushNotification
import com.flexpoker.table.command.events.PlayerForceFoldedEvent
import com.flexpoker.table.query.repository.TableRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class PlayerForceFoldedEventHandler @Inject constructor(
    private val loginRepository: LoginRepository,
    private val tableRepository: TableRepository,
    private val pushNotificationPublisher: PushNotificationPublisher,
    private val chatService: ChatService
) : EventHandler<PlayerForceFoldedEvent> {

    @Async
    override fun handle(event: PlayerForceFoldedEvent) {
        handleUpdatingTable(event)
        handlePushNotifications(event)
        handleChat(event)
    }

    private fun handleUpdatingTable(event: PlayerForceFoldedEvent) {
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
        val updatePots = tableDTO.pots!!
            .map {
                val updatedPotSeats = it.seats.filter { seatName -> seatName != username }.toSet()
                it.copy(seats = updatedPotSeats)
            }.toSet()
        val updatedTable = tableDTO.copy(version = event.version, seats = updatedSeats, pots = updatePots)
        tableRepository.save(updatedTable)
    }

    private fun handlePushNotifications(event: PlayerForceFoldedEvent) {
        val pushNotification = TableUpdatedPushNotification(event.gameId, event.aggregateId)
        pushNotificationPublisher.publish(pushNotification)
    }

    private fun handleChat(event: PlayerForceFoldedEvent) {
        val username = loginRepository.fetchUsernameByAggregateId(event.playerId)
        val message = "Time expired - $username folds"
        chatService.saveAndPushSystemTableChatMessage(event.gameId, event.aggregateId, message)
    }

}