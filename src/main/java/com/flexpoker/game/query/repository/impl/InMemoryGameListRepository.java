package com.flexpoker.game.query.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.flexpoker.game.query.dto.GameInListDTO;
import com.flexpoker.game.query.dto.GameStage;
import com.flexpoker.game.query.repository.GameListRepository;

@Profile("default")
@Repository
public class InMemoryGameListRepository implements GameListRepository {

    private final List<GameInListDTO> gameInListDTOList;

    public InMemoryGameListRepository() {
        gameInListDTOList = new ArrayList<>();
    }

    @Override
    public void saveNew(GameInListDTO gameInListDTO) {
        gameInListDTOList.add(gameInListDTO);
    }

    @Override
    public List<GameInListDTO> fetchAll() {
        return new ArrayList<>(gameInListDTOList);
    }

    @Override
    public void incrementRegisteredPlayers(UUID aggregateId) {
        GameInListDTO existingGameInListDTO = fetchById(aggregateId);
        GameInListDTO updatedGameInListDTO = new GameInListDTO(
                existingGameInListDTO.getId(), existingGameInListDTO.getName(),
                existingGameInListDTO.getStage(),
                existingGameInListDTO.getNumberOfRegisteredPlayers() + 1,
                existingGameInListDTO.getMaxNumberOfPlayers(),
                existingGameInListDTO.getMaxPlayersPerTable(),
                existingGameInListDTO.getBlindLevelIncreaseInMinutes(),
                existingGameInListDTO.getBlindTimerInSeconds(),
                existingGameInListDTO.getCreatedBy(),
                existingGameInListDTO.getCreatedOn());
        removeGame(aggregateId);
        saveNew(updatedGameInListDTO);
    }

    @Override
    public String fetchGameName(UUID aggregateId) {
        GameInListDTO existingGameInListDTO = fetchById(aggregateId);
        return existingGameInListDTO.getName();
    }

    @Override
    public void changeGameStage(UUID aggregateId, GameStage gameStage) {
        GameInListDTO existingGameInListDTO = fetchById(aggregateId);
        GameInListDTO updatedGameInListDTO = new GameInListDTO(
                existingGameInListDTO.getId(), existingGameInListDTO.getName(),
                gameStage.toString(),
                existingGameInListDTO.getNumberOfRegisteredPlayers(),
                existingGameInListDTO.getMaxNumberOfPlayers(),
                existingGameInListDTO.getMaxPlayersPerTable(),
                existingGameInListDTO.getBlindLevelIncreaseInMinutes(),
                existingGameInListDTO.getBlindTimerInSeconds(),
                existingGameInListDTO.getCreatedBy(),
                existingGameInListDTO.getCreatedOn());
        removeGame(aggregateId);
        saveNew(updatedGameInListDTO);
    }

    private GameInListDTO fetchById(UUID aggregateId) {
        return gameInListDTOList.stream().filter(x -> x.getId() == aggregateId).findAny()
                .get();
    }

    private void removeGame(UUID aggregateId) {
        gameInListDTOList.removeIf(x -> x.getId().equals(aggregateId));
    }

}
