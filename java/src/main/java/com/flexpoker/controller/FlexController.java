package com.flexpoker.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flexpoker.model.FlopCards;
import com.flexpoker.model.Game;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.RiverCard;
import com.flexpoker.model.Table;
import com.flexpoker.model.TurnCard;
import com.flexpoker.model.UserGameStatus;

public interface FlexController {

    List<Game> fetchAllGames();

    void createGame(Game game);

    void joinGame(Game game);

    Set<UserGameStatus> fetchAllUserGameStatuses(Game game);

    void verifyRegistrationForGame(Game game);

    PocketCards fetchPocketCards(Game game, Table table);

    Table fetchTable(Game game);

    void verifyGameInProgress(Game game);

    void check(Game game, Table table);

    FlopCards fetchFlopCards(Game game, Table table);

    TurnCard fetchTurnCard(Game game, Table table);

    RiverCard fetchRiverCard(Game game, Table table);

    Map<Integer, PocketCards> fetchRequiredShowCards(Game game, Table table);

    Map<Integer, PocketCards> fetchOptionalShowCards(Game game, Table table);
}
