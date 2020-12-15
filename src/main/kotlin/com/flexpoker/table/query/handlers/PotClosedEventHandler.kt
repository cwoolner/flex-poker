package com.flexpoker.table.query.handlers

import com.flexpoker.framework.event.EventHandler
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher
import com.flexpoker.pushnotifications.TableUpdatedPushNotification
import com.flexpoker.table.command.events.PotClosedEvent
import com.flexpoker.table.query.repository.TableRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class PotClosedEventHandler @Inject constructor(
    private val tableRepository: TableRepository,
    private val pushNotificationPublisher: PushNotificationPublisher
) : EventHandler<PotClosedEvent> {

    @Async
    override fun handle(event: PotClosedEvent) {
        handleUpdatingTable(event)
        handlePushNotifications(event)
    }

    private fun handleUpdatingTable(event: PotClosedEvent) {
        val tableDTO = tableRepository.fetchById(event.aggregateId)
        val updatedPots = tableDTO.pots!!
            .map {
                if (it.isOpen) {
                    it.copy(isOpen = false)
                } else {
                    it
                }
            }.toSet()
        val updatedTable = tableDTO.copy(version = event.version, pots = updatedPots)
        tableRepository.save(updatedTable)
    }

    private fun handlePushNotifications(event: PotClosedEvent) {
        val pushNotification = TableUpdatedPushNotification(event.gameId, event.aggregateId)
        pushNotificationPublisher.publish(pushNotification)
    }

}