package com.flexpoker.table.query.handlers

import com.flexpoker.framework.event.EventHandler
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher
import com.flexpoker.game.query.repository.OpenGameForPlayerRepository
import com.flexpoker.login.repository.LoginRepository
import com.flexpoker.pushnotifications.OpenGamesForPlayerUpdatedPushNotification
import com.flexpoker.pushnotifications.OpenTableForUserPushNotification
import com.flexpoker.table.command.events.TableCreatedEvent
import com.flexpoker.table.query.dto.SeatDTO
import com.flexpoker.table.query.dto.TableDTO
import com.flexpoker.table.query.repository.TableRepository
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class TableCreatedEventHandler @Inject constructor(
    private val loginRepository: LoginRepository,
    private val tableRepository: TableRepository,
    private val openGameForPlayerRepository: OpenGameForPlayerRepository,
    private val pushNotificationPublisher: PushNotificationPublisher
) : EventHandler<TableCreatedEvent> {

    override fun handle(event: TableCreatedEvent) {
        handleNewTableInsert(event)
        handleOpenGameUpdate(event)
        handlePushNotifications(event)
    }

    private fun handleNewTableInsert(event: TableCreatedEvent) {
        val seats = event.seatPositionToPlayerMap.keys
            .map {
                val displayName = loginRepository.fetchUsernameByAggregateId(event.seatPositionToPlayerMap[it!!]!!)
                SeatDTO(it, displayName, event.startingNumberOfChips,
                    0, false, 0, 0, false,
                    false, false, false)
            }
        val tableDTO = TableDTO(event.aggregateId, event.version, seats, 0,
            emptySet(), emptyList(), 0, null)
        tableRepository.save(tableDTO)
    }

    private fun handleOpenGameUpdate(event: TableCreatedEvent) {
        event.seatPositionToPlayerMap.values.forEach {
            openGameForPlayerRepository.assignTableToOpenGame(it!!, event.gameId, event.aggregateId)
        }
    }

    private fun handlePushNotifications(event: TableCreatedEvent) {
        event.seatPositionToPlayerMap.values.forEach {
            val pushNotification = OpenTableForUserPushNotification(event.gameId, event.aggregateId, it!!)
            pushNotificationPublisher.publish(pushNotification)
        }
        event.seatPositionToPlayerMap.values.forEach {
            pushNotificationPublisher.publish(OpenGamesForPlayerUpdatedPushNotification(it!!))
        }
    }

}