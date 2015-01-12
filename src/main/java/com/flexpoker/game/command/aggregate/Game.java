package com.flexpoker.game.command.aggregate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.framework.domain.AggregateRoot;
import com.flexpoker.game.command.events.GameCreatedEvent;
import com.flexpoker.game.command.events.GameFinishedEvent;
import com.flexpoker.game.command.events.GameJoinedEvent;
import com.flexpoker.game.command.events.GameMovedToStartingStageEvent;
import com.flexpoker.game.command.events.GameStartedEvent;
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent;
import com.flexpoker.game.command.events.NewHandIsClearedToStartEvent;
import com.flexpoker.game.command.framework.GameEvent;
import com.flexpoker.model.Blinds;
import com.flexpoker.model.GameStage;

public class Game extends AggregateRoot<GameEvent> {

    private final UUID aggregateId;

    private int aggregateVersion;

    private GameStage gameStage;

    private final Set<UUID> registeredPlayerIds;

    private int maxNumberOfPlayers;

    private int numberOfPlayersPerTable;

    private final Map<UUID, Set<UUID>> tableIdToPlayerIdsMap;

    private Blinds currentBlinds;

    protected Game(UUID aggregateId) {
        this.aggregateId = aggregateId;
        registeredPlayerIds = new HashSet<>();
        tableIdToPlayerIdsMap = new HashMap<>();
    }

    @Override
    public void applyAllHistoricalEvents(List<GameEvent> events) {
        for (GameEvent event : events) {
            aggregateVersion++;
            applyCommonEvent(event);
        }
    }

    @Override
    public void applyAllNewEvents(List<GameEvent> events) {
        for (GameEvent event : events) {
            applyCommonEvent(event);
        }
    }

    private void applyCommonEvent(GameEvent event) {
        switch (event.getType()) {
        case GameCreated:
            applyEvent((GameCreatedEvent) event);
            break;
        case GameJoined:
            applyEvent((GameJoinedEvent) event);
            break;
        case GameMovedToStartingStage:
            applyEvent((GameMovedToStartingStageEvent) event);
            break;
        case GameStarted:
            applyEvent((GameStartedEvent) event);
            break;
        case GameTablesCreatedAndPlayersAssociated:
            applyEvent((GameTablesCreatedAndPlayersAssociatedEvent) event);
            break;
        case GameFinished:
            applyEvent((GameFinishedEvent) event);
            break;
        case NewHandIsClearedToStart:
            break;
        default:
            throw new IllegalArgumentException("Event Type cannot be handled: "
                    + event.getType());
        }
    }

    private void applyEvent(GameCreatedEvent event) {
        gameStage = GameStage.REGISTERING;
        maxNumberOfPlayers = event.getNumberOfPlayers();
        numberOfPlayersPerTable = event.getNumberOfPlayersPerTable();
        addAppliedEvent(event);
    }

    private void applyEvent(GameJoinedEvent event) {
        registeredPlayerIds.add(event.getPlayerId());
        addAppliedEvent(event);
    }

    private void applyEvent(GameMovedToStartingStageEvent event) {
        gameStage = GameStage.STARTING;
        addAppliedEvent(event);
    }

    private void applyEvent(GameTablesCreatedAndPlayersAssociatedEvent event) {
        tableIdToPlayerIdsMap.putAll(event.getTableIdToPlayerIdsMap());
        addAppliedEvent(event);
    }

    private void applyEvent(GameStartedEvent event) {
        currentBlinds = event.getBlinds();
        gameStage = GameStage.INPROGRESS;
        addAppliedEvent(event);
    }

    private void applyEvent(GameFinishedEvent event) {
        gameStage = GameStage.FINISHED;
        addAppliedEvent(event);
    }

    public void createNewGame(String gameName, int maxNumberOfPlayers,
            int numberOfPlayersPerTable, UUID createdByPlayerId) {
        GameCreatedEvent gameCreatedEvent = new GameCreatedEvent(aggregateId,
                ++aggregateVersion, gameName, maxNumberOfPlayers,
                numberOfPlayersPerTable, createdByPlayerId);
        addNewEvent(gameCreatedEvent);
        applyEvent(gameCreatedEvent);
    }

    public void joinGame(UUID playerId) {
        createJoinGameEvent(playerId);

        // if the game is at the max capacity, start the game
        if (registeredPlayerIds.size() == maxNumberOfPlayers) {
            createGameMovedToStartingStageEvent();
            createGameTablesCreatedAndPlayersAssociatedEvent();
            createGameStartedEvent();
        }
    }

    public void attemptToStartNewHand(UUID tableId) {
        // TODO: check for imbalanced tables and such
        NewHandIsClearedToStartEvent event = new NewHandIsClearedToStartEvent(
                aggregateId, ++aggregateVersion, tableId, currentBlinds);
        addNewEvent(event);
        // TOOD: might want to do something for apply, not sure yet
    }

    public void increaseBlinds() {
        // TODO: implement
    }

    private void createJoinGameEvent(UUID playerId) {
        if (gameStage == GameStage.STARTING || gameStage == GameStage.INPROGRESS) {
            throw new FlexPokerException("The game has already started");
        }

        if (gameStage == GameStage.FINISHED) {
            throw new FlexPokerException("The game is already finished.");
        }

        if (registeredPlayerIds.contains(playerId)) {
            throw new FlexPokerException("You are already in this game.");
        }

        GameJoinedEvent event = new GameJoinedEvent(aggregateId, ++aggregateVersion,
                playerId);
        addNewEvent(event);
        applyEvent(event);
    }

    private void createGameMovedToStartingStageEvent() {
        if (gameStage != GameStage.REGISTERING) {
            throw new FlexPokerException(
                    "to move to STARTING, the game stage must be REGISTERING");
        }
        GameMovedToStartingStageEvent event = new GameMovedToStartingStageEvent(
                aggregateId, ++aggregateVersion);
        addNewEvent(event);
        applyEvent(event);
    }

    private void createGameTablesCreatedAndPlayersAssociatedEvent() {
        if (!tableIdToPlayerIdsMap.isEmpty()) {
            throw new FlexPokerException(
                    "tableToPlayerIdsMap should be empty when initializing the tables");
        }
        createTableToPlayerMap();

        GameTablesCreatedAndPlayersAssociatedEvent event = new GameTablesCreatedAndPlayersAssociatedEvent(
                aggregateId, ++aggregateVersion, tableIdToPlayerIdsMap,
                numberOfPlayersPerTable);
        addNewEvent(event);
        applyEvent(event);
    }

    private void createTableToPlayerMap() {
        List<UUID> randomizedListOfPlayerIds = new ArrayList<>(registeredPlayerIds);
        Collections.shuffle(randomizedListOfPlayerIds);

        int numberOfTablesToCreate = determineNumberOfTablesToCreate();
        for (int i = 0; i < numberOfTablesToCreate; i++) {
            tableIdToPlayerIdsMap.put(UUID.randomUUID(), new HashSet<>());
        }

        List<UUID> tableIdList = new ArrayList<>(tableIdToPlayerIdsMap.keySet());

        for (int i = 0; i < registeredPlayerIds.size();) {
            for (int j = 0; j < numberOfTablesToCreate; j++) {
                if (i == registeredPlayerIds.size()) {
                    break;
                }

                tableIdToPlayerIdsMap.get(tableIdList.get(j)).add(
                        randomizedListOfPlayerIds.get(i));
                i++;
            }
        }
    }

    private int determineNumberOfTablesToCreate() {
        int numberOfTables = registeredPlayerIds.size() / numberOfPlayersPerTable;

        // if the number of people doesn't fit perfectly, then an additional
        // table is needed for the overflow
        if (registeredPlayerIds.size() % numberOfPlayersPerTable != 0) {
            numberOfTables++;
        }

        return numberOfTables;
    }

    private void createGameStartedEvent() {
        GameStartedEvent event = new GameStartedEvent(aggregateId, ++aggregateVersion,
                tableIdToPlayerIdsMap.keySet(), new Blinds(10, 20));
        addNewEvent(event);
        applyEvent(event);
    }

}
