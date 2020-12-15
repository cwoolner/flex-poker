package com.flexpoker.table.query.handlers

import com.flexpoker.framework.event.EventHandler
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher
import com.flexpoker.pushnotifications.TableUpdatedPushNotification
import com.flexpoker.table.command.events.TurnCardDealtEvent
import com.flexpoker.table.query.dto.CardDTO
import com.flexpoker.table.query.repository.CardsUsedInHandRepository
import com.flexpoker.table.query.repository.TableRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class TurnCardDealtEventHandler @Inject constructor(
    private val tableRepository: TableRepository,
    private val cardsUsedInHandRepository: CardsUsedInHandRepository,
    private val pushNotificationPublisher: PushNotificationPublisher
) : EventHandler<TurnCardDealtEvent> {

    @Async
    override fun handle(event: TurnCardDealtEvent) {
        handleUpdatingTable(event)
        handlePushNotifications(event)
    }

    private fun handleUpdatingTable(event: TurnCardDealtEvent) {
        val tableDTO = tableRepository.fetchById(event.aggregateId)
        val turnCard = cardsUsedInHandRepository.fetchTurnCard(event.handId)
        val visibleCommonCards = tableDTO.visibleCommonCards!!.plus(CardDTO(turnCard.card.id))
        val updatedTable = tableDTO.copy(version = event.version, visibleCommonCards = visibleCommonCards)
        tableRepository.save(updatedTable)
    }

    private fun handlePushNotifications(event: TurnCardDealtEvent) {
        val pushNotification = TableUpdatedPushNotification(event.gameId, event.aggregateId)
        pushNotificationPublisher.publish(pushNotification)
    }

}