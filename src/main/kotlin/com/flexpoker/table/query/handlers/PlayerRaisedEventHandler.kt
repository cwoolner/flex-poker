package com.flexpoker.table.query.handlers

import com.flexpoker.chat.service.ChatService
import com.flexpoker.framework.event.EventHandler
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher
import com.flexpoker.login.repository.LoginRepository
import com.flexpoker.pushnotifications.TableUpdatedPushNotification
import com.flexpoker.table.command.events.PlayerRaisedEvent
import com.flexpoker.table.query.repository.TableRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class PlayerRaisedEventHandler @Inject constructor(
    private val loginRepository: LoginRepository,
    private val tableRepository: TableRepository,
    private val pushNotificationPublisher: PushNotificationPublisher,
    private val chatService: ChatService
) : EventHandler<PlayerRaisedEvent> {

    @Async
    override fun handle(event: PlayerRaisedEvent) {
        handleUpdatingTable(event)
        handlePushNotifications(event)
        handleChat(event)
    }

    private fun handleUpdatingTable(event: PlayerRaisedEvent) {
        val tableDTO = tableRepository.fetchById(event.aggregateId)
        val username = loginRepository.fetchUsernameByAggregateId(event.playerId)
        val raiseToAmount = event.raiseToAmount
        val updatedSeats = tableDTO.seats!!
            .map {
                val amountOverChipsInFront = raiseToAmount - it.chipsInFront
                if (it.name == username) {
                    val updatedChipsInBack = it.chipsInBack - amountOverChipsInFront
                    it.copy(chipsInBack = updatedChipsInBack, chipsInFront = raiseToAmount,
                        raiseTo = 0, callAmount = 0, isActionOn = false)
                } else {
                    it.copy(raiseTo = minOf(raiseToAmount * 2, it.chipsInFront + it.chipsInBack),
                        callAmount = amountOverChipsInFront)
                }
            }
        val previousChipsInFront = tableDTO.seats.first { it.name == username }.chipsInFront
        val totalPotIncrease = raiseToAmount - previousChipsInFront
        val updatedTable = tableDTO.copy(version = event.version, seats = updatedSeats,
            totalPot = tableDTO.totalPot + totalPotIncrease)
        tableRepository.save(updatedTable)
    }

    private fun handlePushNotifications(event: PlayerRaisedEvent) {
        val pushNotification = TableUpdatedPushNotification(event.gameId, event.aggregateId)
        pushNotificationPublisher.publish(pushNotification)
    }

    private fun handleChat(event: PlayerRaisedEvent) {
        val username = loginRepository.fetchUsernameByAggregateId(event.playerId)
        val message = username + " raises to " + event.raiseToAmount
        chatService.saveAndPushSystemTableChatMessage(event.gameId, event.aggregateId, message)
    }

}