package com.flexpoker.table.query.handlers

import com.flexpoker.framework.event.EventHandler
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher
import com.flexpoker.login.repository.LoginRepository
import com.flexpoker.pushnotifications.TableUpdatedPushNotification
import com.flexpoker.table.command.events.ActionOnChangedEvent
import com.flexpoker.table.query.repository.TableRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class ActionOnChangedEventHandler @Inject constructor(
    private val loginRepository: LoginRepository,
    private val tableRepository: TableRepository,
    private val pushNotificationPublisher: PushNotificationPublisher
) : EventHandler<ActionOnChangedEvent> {

    @Async
    override fun handle(event: ActionOnChangedEvent) {
        handleUpdatingTable(event)
        handlePushNotifications(event)
    }

    private fun handleUpdatingTable(event: ActionOnChangedEvent) {
        val tableDTO = tableRepository.fetchById(event.aggregateId)
        val username = loginRepository.fetchUsernameByAggregateId(event.playerId)
        val updatedSeats = tableDTO.seats!!.map { it.copy(isActionOn = it.name == username) }
        val updatedTable = tableDTO.copy(version = event.version, seats = updatedSeats)
        tableRepository.save(updatedTable)
    }

    private fun handlePushNotifications(event: ActionOnChangedEvent) {
        val pushNotification = TableUpdatedPushNotification(event.gameId, event.aggregateId)
        pushNotificationPublisher.publish(pushNotification)
    }

}