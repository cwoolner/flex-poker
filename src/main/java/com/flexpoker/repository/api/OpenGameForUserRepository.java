package com.flexpoker.repository.api;

import java.util.List;
import java.util.UUID;

import com.flexpoker.model.GameStage;
import com.flexpoker.model.OpenGameForUser;

public interface OpenGameForUserRepository {

    List<OpenGameForUser> fetchAllOpenGamesForUser(String username);

    void deleteOpenGameForUser(String username, UUID gameId);

    void addOpenGameForUser(String username, OpenGameForUser openGameForUser);

    void setGameStage(String username, UUID gameId, GameStage gameStage);

}
