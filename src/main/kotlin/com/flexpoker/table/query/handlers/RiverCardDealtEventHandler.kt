package com.flexpoker.table.query.handlers

import com.flexpoker.framework.event.EventHandler
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher
import com.flexpoker.pushnotifications.TableUpdatedPushNotification
import com.flexpoker.table.command.events.RiverCardDealtEvent
import com.flexpoker.table.query.dto.CardDTO
import com.flexpoker.table.query.repository.CardsUsedInHandRepository
import com.flexpoker.table.query.repository.TableRepository
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class RiverCardDealtEventHandler @Inject constructor(
    private val tableRepository: TableRepository,
    private val cardsUsedInHandRepository: CardsUsedInHandRepository,
    private val pushNotificationPublisher: PushNotificationPublisher
) : EventHandler<RiverCardDealtEvent> {

    override fun handle(event: RiverCardDealtEvent) {
        handleUpdatingTable(event)
        handlePushNotifications(event)
    }

    private fun handleUpdatingTable(event: RiverCardDealtEvent) {
        val tableDTO = tableRepository.fetchById(event.aggregateId)
        val riverCard = cardsUsedInHandRepository.fetchRiverCard(event.handId)
        val visibleCommonCards = tableDTO.visibleCommonCards!!.plus(CardDTO(riverCard.card.id))
        val updatedTable = tableDTO.copy(version = event.version, visibleCommonCards = visibleCommonCards)
        tableRepository.save(updatedTable)
    }

    private fun handlePushNotifications(event: RiverCardDealtEvent) {
        val pushNotification = TableUpdatedPushNotification(event.gameId, event.aggregateId)
        pushNotificationPublisher.publish(pushNotification)
    }

}