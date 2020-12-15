package com.flexpoker.table.query.handlers

import com.flexpoker.framework.event.EventHandler
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher
import com.flexpoker.pushnotifications.TableUpdatedPushNotification
import com.flexpoker.table.command.events.WinnersDeterminedEvent
import com.flexpoker.table.query.repository.TableRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class WinnersDeterminedEventHandler @Inject constructor(
    private val tableRepository: TableRepository,
    private val pushNotificationPublisher: PushNotificationPublisher
) : EventHandler<WinnersDeterminedEvent> {

    @Async
    override fun handle(event: WinnersDeterminedEvent) {
        handleUpdatingTable(event)
        handlePushNotifications(event)
    }

    private fun handleUpdatingTable(event: WinnersDeterminedEvent) {
        val tableDTO = tableRepository.fetchById(event.aggregateId)
        val updatedTable = tableDTO.copy(version = event.version)
        tableRepository.save(updatedTable)
    }

    private fun handlePushNotifications(event: WinnersDeterminedEvent) {
        val pushNotification = TableUpdatedPushNotification(event.gameId, event.aggregateId)
        pushNotificationPublisher.publish(pushNotification)
    }

}