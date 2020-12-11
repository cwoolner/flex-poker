package com.flexpoker.table.command.handlers

import com.flexpoker.framework.command.CommandHandler
import com.flexpoker.framework.event.EventPublisher
import com.flexpoker.table.command.commands.StartNewHandForNewGameCommand
import com.flexpoker.table.command.factory.TableFactory
import com.flexpoker.table.command.framework.TableEvent
import com.flexpoker.table.command.repository.TableEventRepository
import com.flexpoker.table.command.service.CardService
import com.flexpoker.table.command.service.HandEvaluatorService
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class StartNewHandForNewGameCommandHandler @Inject constructor(
    private val tableFactory: TableFactory,
    private val eventPublisher: EventPublisher<TableEvent>,
    private val tableEventRepository: TableEventRepository,
    private val cardService: CardService,
    private val handEvaluatorService: HandEvaluatorService
) : CommandHandler<StartNewHandForNewGameCommand> {

    @Async
    override fun handle(command: StartNewHandForNewGameCommand) {
        val existingEvents = tableEventRepository.fetchAll(command.tableId)
        val table = tableFactory.createFrom(existingEvents)
        val shuffledDeckOfCards = cardService.createShuffledDeck()
        val cardsUsedInHand = cardService.createCardsUsedInHand(shuffledDeckOfCards, table.numberOfPlayersAtTable)
        val possibleHandRankings = handEvaluatorService.determinePossibleHands(
            cardsUsedInHand.flopCards, cardsUsedInHand.turnCard, cardsUsedInHand.riverCard)
        val handEvaluations = handEvaluatorService.determineHandEvaluation(
            cardsUsedInHand.flopCards, cardsUsedInHand.turnCard, cardsUsedInHand.riverCard,
            cardsUsedInHand.pocketCards, possibleHandRankings)
        table.startNewHandForNewGame(command.smallBlind, command.bigBlind, shuffledDeckOfCards,
            cardsUsedInHand, handEvaluations)
        val newEvents = table.fetchNewEvents()
        val newlySavedEventsWithVersions = tableEventRepository.setEventVersionsAndSave(existingEvents.size, newEvents)
        newlySavedEventsWithVersions.forEach { eventPublisher.publish(it) }
    }

}