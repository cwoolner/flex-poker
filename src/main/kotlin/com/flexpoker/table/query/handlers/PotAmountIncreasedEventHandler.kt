package com.flexpoker.table.query.handlers

import com.flexpoker.framework.event.EventHandler
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher
import com.flexpoker.pushnotifications.TableUpdatedPushNotification
import com.flexpoker.table.command.events.PotAmountIncreasedEvent
import com.flexpoker.table.query.repository.TableRepository
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class PotAmountIncreasedEventHandler @Inject constructor(
    private val tableRepository: TableRepository,
    private val pushNotificationPublisher: PushNotificationPublisher
) : EventHandler<PotAmountIncreasedEvent> {

    override fun handle(event: PotAmountIncreasedEvent) {
        handleUpdatingTable(event)
        handlePushNotifications(event)
    }

    private fun handleUpdatingTable(event: PotAmountIncreasedEvent) {
        val tableDTO = tableRepository.fetchById(event.aggregateId)
        val updatedPots = tableDTO.pots!!
            .map {
                if (it.isOpen) {
                    it.copy(amount = it.amount + event.amountIncreased)
                } else {
                    it
                }
            }.toSet()
        val updatedTable = tableDTO.copy(version = event.version, pots = updatedPots)
        tableRepository.save(updatedTable)
    }

    private fun handlePushNotifications(event: PotAmountIncreasedEvent) {
        val pushNotification = TableUpdatedPushNotification(event.gameId, event.aggregateId)
        pushNotificationPublisher.publish(pushNotification)
    }

}