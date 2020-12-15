package com.flexpoker.table.query.handlers

import com.flexpoker.framework.event.EventHandler
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher
import com.flexpoker.login.repository.LoginRepository
import com.flexpoker.pushnotifications.SendUserPocketCardsPushNotification
import com.flexpoker.pushnotifications.TableUpdatedPushNotification
import com.flexpoker.table.command.events.HandDealtEvent
import com.flexpoker.table.query.dto.SeatDTO
import com.flexpoker.table.query.repository.CardsUsedInHandRepository
import com.flexpoker.table.query.repository.TableRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class HandDealtEventHandler @Inject constructor(
    private val loginRepository: LoginRepository,
    private val tableRepository: TableRepository,
    private val cardsUsedInHandRepository: CardsUsedInHandRepository,
    private val pushNotificationPublisher: PushNotificationPublisher
) : EventHandler<HandDealtEvent> {

    @Async
    override fun handle(event: HandDealtEvent) {
        handleCardsUsedInHandStorage(event)
        handleUpdatingTable(event)
        handlePushNotifications(event)
    }

    private fun handleCardsUsedInHandStorage(event: HandDealtEvent) {
        cardsUsedInHandRepository.saveFlopCards(event.handId, event.flopCards)
        cardsUsedInHandRepository.saveTurnCard(event.handId, event.turnCard)
        cardsUsedInHandRepository.saveRiverCard(event.handId, event.riverCard)
        cardsUsedInHandRepository.savePocketCards(event.handId, event.playerToPocketCardsMap)
    }

    private fun handleUpdatingTable(event: HandDealtEvent) {
        val tableDTO = tableRepository.fetchById(event.aggregateId)
        val seats = event.playersStillInHand.map {
            val position = event.seatMap.filterValues { x -> x == it }.keys.first()
            val name = loginRepository.fetchUsernameByAggregateId(it)
            val chipsInBack = event.chipsInBack[it]!!
            val chipsInFront = event.chipsInFrontMap[it]!!
            val raiseTo = event.raiseToAmountsMap[it]!!
            val callAmount = event.callAmountsMap[it]!!
            val button = event.buttonOnPosition == position
            val smallBlind = event.smallBlindPosition == position
            val bigBlind = event.bigBlindPosition == position
            SeatDTO(position, name, chipsInBack, chipsInFront, true,
                raiseTo, callAmount, button, smallBlind, bigBlind, false)
        }
        val updatedTable = tableDTO.copy(
            version = event.version,
            seats = seats,
            currentHandId = event.handId,
            totalPot = event.chipsInFrontMap.values.sum(),
            pots = emptySet(),
            visibleCommonCards = emptyList(),
            currentHandMinRaiseToAmount = event.bigBlind
        )
        tableRepository.save(updatedTable)
    }

    private fun handlePushNotifications(event: HandDealtEvent) {
        val pushNotification = TableUpdatedPushNotification(event.gameId, event.aggregateId)
        pushNotificationPublisher.publish(pushNotification)

        event.playersStillInHand.forEach {
            pushNotificationPublisher.publish(SendUserPocketCardsPushNotification(
                it!!, event.handId, event.playerToPocketCardsMap[it]!!))
        }
    }

}