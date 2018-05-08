package com.flexpoker.table.command.handlers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.CommandHandler;
import com.flexpoker.framework.event.EventPublisher;
import com.flexpoker.table.command.commands.StartNewHandForExistingTableCommand;
import com.flexpoker.table.command.factory.TableFactory;
import com.flexpoker.table.command.framework.TableEvent;
import com.flexpoker.table.command.repository.TableEventRepository;
import com.flexpoker.table.command.service.CardService;
import com.flexpoker.table.command.service.HandEvaluatorService;

@Component
public class StartNewHandForExistingTableCommandHandler implements
        CommandHandler<StartNewHandForExistingTableCommand> {

    private final TableFactory tableFactory;

    private final EventPublisher<TableEvent> eventPublisher;

    private final TableEventRepository tableEventRepository;

    private final CardService cardService;

    private final HandEvaluatorService handEvaluatorService;

    @Inject
    public StartNewHandForExistingTableCommandHandler(TableFactory tableFactory,
            EventPublisher<TableEvent> eventPublisher,
            TableEventRepository tableEventRepository, CardService cardService,
            HandEvaluatorService handEvaluatorService) {
        this.tableFactory = tableFactory;
        this.eventPublisher = eventPublisher;
        this.tableEventRepository = tableEventRepository;
        this.cardService = cardService;
        this.handEvaluatorService = handEvaluatorService;
    }

    @Async
    @Override
    public void handle(StartNewHandForExistingTableCommand command) {
        var tableEvents = tableEventRepository.fetchAll(command.getTableId());
        var table = tableFactory.createFrom(tableEvents);

        var shuffledDeckOfCards = cardService.createShuffledDeck();
        var cardsUsedInHand = cardService.createCardsUsedInHand(shuffledDeckOfCards, table.getNumberOfPlayersAtTable());

        var possibleHandRankings = handEvaluatorService.determinePossibleHands(
                cardsUsedInHand.getFlopCards(), cardsUsedInHand.getTurnCard(), cardsUsedInHand.getRiverCard());
        var handEvaluations = handEvaluatorService.determineHandEvaluation(
                cardsUsedInHand.getFlopCards(), cardsUsedInHand.getTurnCard(), cardsUsedInHand.getRiverCard(),
                cardsUsedInHand.getPocketCards(), possibleHandRankings);

        table.startNewHandForExistingTable(command.getSmallBlind(),
                command.getBigBlind(), shuffledDeckOfCards, cardsUsedInHand,
                handEvaluations);
        table.fetchNewEvents().forEach(x -> tableEventRepository.save(x));
        table.fetchNewEvents().forEach(x -> eventPublisher.publish(x));
    }

}
