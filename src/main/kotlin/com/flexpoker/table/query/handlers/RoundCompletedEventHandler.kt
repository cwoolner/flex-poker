package com.flexpoker.table.query.handlers

import com.flexpoker.framework.event.EventHandler
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher
import com.flexpoker.pushnotifications.TableUpdatedPushNotification
import com.flexpoker.table.command.events.RoundCompletedEvent
import com.flexpoker.table.query.repository.TableRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class RoundCompletedEventHandler @Inject constructor(
    private val tableRepository: TableRepository,
    private val pushNotificationPublisher: PushNotificationPublisher
) : EventHandler<RoundCompletedEvent> {

    @Async
    override fun handle(event: RoundCompletedEvent) {
        handleUpdatingTable(event)
        handlePushNotifications(event)
    }

    private fun handleUpdatingTable(event: RoundCompletedEvent) {
        val tableDTO = tableRepository.fetchById(event.aggregateId)
        val updatedSeats = tableDTO.seats!!
            .map {
                it.copy(chipsInFront = 0, raiseTo = minOf(it.chipsInBack, tableDTO.currentHandMinRaiseToAmount))
            }
        val updatedTable = tableDTO.copy(version = event.version, seats = updatedSeats)
        tableRepository.save(updatedTable)
    }

    private fun handlePushNotifications(event: RoundCompletedEvent) {
        val pushNotification = TableUpdatedPushNotification(event.gameId, event.aggregateId)
        pushNotificationPublisher.publish(pushNotification)
    }

}