package com.flexpoker.web.translator;

import java.util.ArrayList;
import java.util.List;

import com.flexpoker.model.Game;
import com.flexpoker.web.model.AvailableTournamentListViewModel;

public class GameListTranslator {

    public List<AvailableTournamentListViewModel> translate(List<Game> gameList) {

        List<AvailableTournamentListViewModel> availableTournamentList = new ArrayList<>();

        for (Game game : gameList) {
            AvailableTournamentListViewModel availableTournament = new AvailableTournamentListViewModel(
                    game.getId().toString(), game.getTotalPlayers(), game.getTotalPlayers(),
                    game.getMaxPlayersPerTable(), game.getCreatedByUser().getUsername(),
                    game.getCreatedOn());
            availableTournamentList.add(availableTournament);            
        }

        return availableTournamentList;
    }

}
