package com.flexpoker.web.notifier;

import java.util.List;

import javax.inject.Inject;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.flexpoker.bso.api.GameBso;
import com.flexpoker.config.Notifier;
import com.flexpoker.event.GameListUpdatedEvent;
import com.flexpoker.util.MessagingConstants;
import com.flexpoker.web.model.AvailableTournamentListViewModel;
import com.flexpoker.web.translator.GameListTranslator;

@Notifier
public class GameListUpdatedNotifier implements ApplicationListener<GameListUpdatedEvent> {

    private final GameBso gameBso;
    
    private final SimpMessageSendingOperations messagingTemplate;
    
    @Inject
    public GameListUpdatedNotifier(GameBso gameBso,
            SimpMessageSendingOperations messagingTemplate) {
        this.gameBso = gameBso;
        this.messagingTemplate = messagingTemplate;
    }
    
    @Override
    public void onApplicationEvent(GameListUpdatedEvent event) {
        List<AvailableTournamentListViewModel> allGames =
                new GameListTranslator().translate(gameBso.fetchAllGames());
        messagingTemplate.convertAndSend(MessagingConstants.GAMES_UPDATED, allGames);
    }

}
