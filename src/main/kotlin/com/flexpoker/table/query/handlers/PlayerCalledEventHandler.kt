package com.flexpoker.table.query.handlers

import com.flexpoker.chat.service.ChatService
import com.flexpoker.framework.event.EventHandler
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher
import com.flexpoker.login.repository.LoginRepository
import com.flexpoker.pushnotifications.TableUpdatedPushNotification
import com.flexpoker.table.command.events.PlayerCalledEvent
import com.flexpoker.table.query.repository.TableRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class PlayerCalledEventHandler @Inject constructor(
    private val loginRepository: LoginRepository,
    private val tableRepository: TableRepository,
    private val pushNotificationPublisher: PushNotificationPublisher,
    private val chatService: ChatService
) : EventHandler<PlayerCalledEvent> {

    @Async
    override fun handle(event: PlayerCalledEvent) {
        handleUpdatingTable(event)
        handlePushNotifications(event)
        handleChat(event)
    }

    private fun handleUpdatingTable(event: PlayerCalledEvent) {
        val tableDTO = tableRepository.fetchById(event.aggregateId)
        val username = loginRepository.fetchUsernameByAggregateId(event.playerId)
        val updatedSeats = tableDTO.seats!!
            .map {
                if (it.name == username) {
                    val callingAmount = it.callAmount
                    val updatedChipsInFront = it.chipsInFront + callingAmount
                    val updatedChipsInBack = it.chipsInBack - callingAmount
                    it.copy(chipsInFront = updatedChipsInFront, chipsInBack = updatedChipsInBack,
                        raiseTo = 0, callAmount = 0, isActionOn = false)
                } else {
                    it
                }
            }
        val callAmount = tableDTO.seats.first { it.name == username }.callAmount
        val updatedTable = tableDTO.copy(version = event.version, seats = updatedSeats,
            totalPot = tableDTO.totalPot + callAmount)
        tableRepository.save(updatedTable)
    }

    private fun handlePushNotifications(event: PlayerCalledEvent) {
        val pushNotification = TableUpdatedPushNotification(event.gameId, event.aggregateId)
        pushNotificationPublisher.publish(pushNotification)
    }

    private fun handleChat(event: PlayerCalledEvent) {
        val username = loginRepository.fetchUsernameByAggregateId(event.playerId)
        val message = "$username calls"
        chatService.saveAndPushSystemTableChatMessage(event.gameId, event.aggregateId, message)
    }

}