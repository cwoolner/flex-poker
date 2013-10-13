package com.flexpoker.web.translator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.flexpoker.model.Game;
import com.flexpoker.web.model.AvailableTournamentListViewModel;

public class GameListTranslator {

    public List<AvailableTournamentListViewModel> translate(List<Game> gameList) {

        List<AvailableTournamentListViewModel> availableTournamentList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a MM/dd/yyyy");
        
        for (Game game : gameList) {
            AvailableTournamentListViewModel availableTournament = new AvailableTournamentListViewModel(
                    game.getId(), game.getName(), game.getUserGameStatuses().size(),
                    game.getTotalPlayers(), game.getMaxPlayersPerTable(),
                    game.getCreatedByUser().getUsername(), dateFormat.format(game.getCreatedOn()));
            availableTournamentList.add(availableTournament);
        }

        return availableTournamentList;
    }

}
