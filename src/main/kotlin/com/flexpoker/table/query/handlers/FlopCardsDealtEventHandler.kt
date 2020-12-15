package com.flexpoker.table.query.handlers

import com.flexpoker.framework.event.EventHandler
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher
import com.flexpoker.pushnotifications.TableUpdatedPushNotification
import com.flexpoker.table.command.events.FlopCardsDealtEvent
import com.flexpoker.table.query.dto.CardDTO
import com.flexpoker.table.query.repository.CardsUsedInHandRepository
import com.flexpoker.table.query.repository.TableRepository
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class FlopCardsDealtEventHandler @Inject constructor(
    private val tableRepository: TableRepository,
    private val cardsUsedInHandRepository: CardsUsedInHandRepository,
    private val pushNotificationPublisher: PushNotificationPublisher
) : EventHandler<FlopCardsDealtEvent> {

    override fun handle(event: FlopCardsDealtEvent) {
        handleUpdatingTable(event)
        handlePushNotifications(event)
    }

    private fun handleUpdatingTable(event: FlopCardsDealtEvent) {
        val tableDTO = tableRepository.fetchById(event.aggregateId)
        val (card1, card2, card3) = cardsUsedInHandRepository.fetchFlopCards(event.handId)
        val visibleCommonCards = listOf(CardDTO(card1.id), CardDTO(card2.id), CardDTO(card3.id))
        val updatedTable = tableDTO.copy(version = event.version, visibleCommonCards = visibleCommonCards)
        tableRepository.save(updatedTable)
    }

    private fun handlePushNotifications(event: FlopCardsDealtEvent) {
        val pushNotification = TableUpdatedPushNotification(event.gameId, event.aggregateId)
        pushNotificationPublisher.publish(pushNotification)
    }

}