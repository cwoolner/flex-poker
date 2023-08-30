package com.flexpoker.table.query.handlers

import com.flexpoker.framework.event.EventHandler
import com.flexpoker.table.command.events.HandCompletedEvent
import com.flexpoker.table.query.repository.CardsUsedInHandRepository
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class HandCompletedEventHandler @Inject constructor(
    private val cardsUsedInHandRepository: CardsUsedInHandRepository,
) : EventHandler<HandCompletedEvent> {

    override fun handle(event: HandCompletedEvent) {
        cardsUsedInHandRepository.removeHand(event.handId)
    }

}