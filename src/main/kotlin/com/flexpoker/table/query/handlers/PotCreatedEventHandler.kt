package com.flexpoker.table.query.handlers

import com.flexpoker.framework.event.EventHandler
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher
import com.flexpoker.login.repository.LoginRepository
import com.flexpoker.pushnotifications.TableUpdatedPushNotification
import com.flexpoker.table.command.events.PotCreatedEvent
import com.flexpoker.table.query.dto.PotDTO
import com.flexpoker.table.query.repository.TableRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class PotCreatedEventHandler @Inject constructor(
    private val tableRepository: TableRepository,
    private val pushNotificationPublisher: PushNotificationPublisher,
    private val loginRepository: LoginRepository
) : EventHandler<PotCreatedEvent> {

    @Async
    override fun handle(event: PotCreatedEvent) {
        handleUpdatingTable(event)
        handlePushNotifications(event)
    }

    private fun handleUpdatingTable(event: PotCreatedEvent) {
        val tableDTO = tableRepository.fetchById(event.aggregateId)
        val playerUsernames = event.playersInvolved.map { loginRepository.fetchUsernameByAggregateId(it) }.toSet()
        val updatedPots = tableDTO.pots!!.plus(PotDTO(playerUsernames, 0, true, emptySet()))
        val updatedTable = tableDTO.copy(version = event.version, pots = updatedPots)
        tableRepository.save(updatedTable)
    }

    private fun handlePushNotifications(event: PotCreatedEvent) {
        val pushNotification = TableUpdatedPushNotification(event.gameId, event.aggregateId)
        pushNotificationPublisher.publish(pushNotification)
    }

}