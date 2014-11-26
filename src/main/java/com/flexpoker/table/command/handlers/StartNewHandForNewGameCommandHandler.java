package com.flexpoker.table.command.handlers;

import java.util.List;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.CommandHandler;
import com.flexpoker.framework.event.EventPublisher;
import com.flexpoker.model.card.Card;
import com.flexpoker.model.card.CardsUsedInHand;
import com.flexpoker.table.command.aggregate.Table;
import com.flexpoker.table.command.commands.StartNewHandForNewGameCommand;
import com.flexpoker.table.command.factory.TableFactory;
import com.flexpoker.table.command.framework.TableEvent;
import com.flexpoker.table.command.framework.TableEventType;
import com.flexpoker.table.command.repository.TableEventRepository;
import com.flexpoker.table.command.service.CardService;

@Component
public class StartNewHandForNewGameCommandHandler implements
        CommandHandler<StartNewHandForNewGameCommand> {

    private final TableFactory tableFactory;

    private final EventPublisher<TableEventType> eventPublisher;

    private final TableEventRepository tableEventRepository;

    private final CardService cardService;

    @Inject
    public StartNewHandForNewGameCommandHandler(TableFactory tableFactory,
            EventPublisher<TableEventType> eventPublisher,
            TableEventRepository tableEventRepository, CardService cardService) {
        this.tableFactory = tableFactory;
        this.eventPublisher = eventPublisher;
        this.tableEventRepository = tableEventRepository;
        this.cardService = cardService;
    }

    @Async
    @Override
    public void handle(StartNewHandForNewGameCommand command) {
        List<TableEvent> tableEvents = tableEventRepository
                .fetchAll(command.getTableId());
        Table table = tableFactory.createFrom(tableEvents);

        List<Card> shuffledDeckOfCards = cardService.createShuffledDeck();
        CardsUsedInHand cardsUsedInHand = cardService.createCardsUsedInHand(
                shuffledDeckOfCards, table.getNumberOfPlayersAtTable());

        table.startNewHandForNewGame(shuffledDeckOfCards, cardsUsedInHand);
        table.fetchNewEvents().forEach(x -> eventPublisher.publish(x));
        table.fetchNewEvents().forEach(x -> tableEventRepository.save(x));
    }

}
