package com.flexpoker.table.command.handlers

import com.flexpoker.framework.command.CommandHandler
import com.flexpoker.framework.event.EventPublisher
import com.flexpoker.table.command.aggregate.applyEvents
import com.flexpoker.table.command.aggregate.eventproducers.startNewHandForNewGame
import com.flexpoker.table.command.aggregate.numberOfPlayersAtTable
import com.flexpoker.table.command.commands.StartNewHandForNewGameCommand
import com.flexpoker.table.command.events.TableEvent
import com.flexpoker.table.command.repository.TableEventRepository
import com.flexpoker.table.command.service.CardService
import com.flexpoker.table.command.service.HandEvaluatorService
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class StartNewHandForNewGameCommandHandler @Inject constructor(
    private val eventPublisher: EventPublisher<TableEvent>,
    private val tableEventRepository: TableEventRepository,
    private val cardService: CardService,
    private val handEvaluatorService: HandEvaluatorService
) : CommandHandler<StartNewHandForNewGameCommand> {

    @Async
    override fun handle(command: StartNewHandForNewGameCommand) {
        val existingEvents = tableEventRepository.fetchAll(command.tableId)
        val state = applyEvents(existingEvents)
        val shuffledDeckOfCards = cardService.createShuffledDeck()
        val cardsUsedInHand = cardService.createCardsUsedInHand(shuffledDeckOfCards, numberOfPlayersAtTable(state))
        val possibleHandRankings = handEvaluatorService.determinePossibleHands(
            cardsUsedInHand.flopCards, cardsUsedInHand.turnCard, cardsUsedInHand.riverCard)
        val handEvaluations = handEvaluatorService.determineHandEvaluation(
            cardsUsedInHand.flopCards, cardsUsedInHand.turnCard, cardsUsedInHand.riverCard,
            cardsUsedInHand.pocketCards, possibleHandRankings)
        val newEvents = startNewHandForNewGame(state, command.smallBlind, command.bigBlind,
            shuffledDeckOfCards, cardsUsedInHand, handEvaluations)
        val newlySavedEventsWithVersions = tableEventRepository.setEventVersionsAndSave(existingEvents.size, newEvents)
        newlySavedEventsWithVersions.forEach { eventPublisher.publish(it) }
    }

}