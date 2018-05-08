package com.flexpoker.game.command.aggregate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.framework.command.EventApplier;
import com.flexpoker.framework.domain.AggregateRoot;
import com.flexpoker.game.command.events.BlindsIncreasedEvent;
import com.flexpoker.game.command.events.GameCreatedEvent;
import com.flexpoker.game.command.events.GameFinishedEvent;
import com.flexpoker.game.command.events.GameJoinedEvent;
import com.flexpoker.game.command.events.GameMovedToStartingStageEvent;
import com.flexpoker.game.command.events.GameStartedEvent;
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent;
import com.flexpoker.game.command.events.NewHandIsClearedToStartEvent;
import com.flexpoker.game.command.events.PlayerBustedGameEvent;
import com.flexpoker.game.command.events.PlayerMovedToNewTableEvent;
import com.flexpoker.game.command.events.TablePausedForBalancingEvent;
import com.flexpoker.game.command.events.TableRemovedEvent;
import com.flexpoker.game.command.events.TableResumedAfterBalancingEvent;
import com.flexpoker.game.command.framework.GameEvent;
import com.flexpoker.game.query.dto.GameStage;

public class Game extends AggregateRoot<GameEvent> {

    private final UUID aggregateId;

    private int aggregateVersion;

    private final Map<Class<? extends GameEvent>, EventApplier<? super GameEvent>> methodTable;

    private GameStage gameStage;

    private final Set<UUID> registeredPlayerIds;

    private final int maxNumberOfPlayers;

    private final int numberOfPlayersPerTable;

    private final Map<UUID, Set<UUID>> tableIdToPlayerIdsMap;

    private final BlindSchedule blindSchedule;

    private final TableBalancer tableBalancer;

    private final Set<UUID> pausedTablesForBalancing;

    protected Game(boolean creatingFromEvents, UUID aggregateId,
            String gameName, int maxNumberOfPlayers,
            int numberOfPlayersPerTable, int numberOfSecondsForActionOnTimer,
            UUID createdById, GameStage gameStage, BlindSchedule blindSchedule,
            TableBalancer tableBalancer) {
        this.aggregateId = aggregateId;
        this.maxNumberOfPlayers = maxNumberOfPlayers;
        this.numberOfPlayersPerTable = numberOfPlayersPerTable;
        this.blindSchedule = blindSchedule;
        this.tableBalancer = tableBalancer;
        this.gameStage = gameStage;
        registeredPlayerIds = new HashSet<>();
        tableIdToPlayerIdsMap = new HashMap<>();
        pausedTablesForBalancing = new HashSet<>();

        methodTable = new HashMap<>();
        populateMethodTable();

        if (!creatingFromEvents) {
            var gameCreatedEvent = new GameCreatedEvent(
                    aggregateId, ++aggregateVersion, gameName,
                    maxNumberOfPlayers, numberOfPlayersPerTable, createdById,
                    blindSchedule.getNumberOfMinutesBetweenLevels(),
                    numberOfSecondsForActionOnTimer);
            addNewEvent(gameCreatedEvent);
            applyCommonEvent(gameCreatedEvent);
        }
    }

    @Override
    public void applyAllHistoricalEvents(List<GameEvent> events) {
        for (GameEvent event : events) {
            aggregateVersion++;
            applyCommonEvent(event);
        }
    }

    private void populateMethodTable() {
        methodTable.put(GameCreatedEvent.class, x -> {});
        methodTable.put(GameJoinedEvent.class, x -> registeredPlayerIds
                .add(((GameJoinedEvent) x).getPlayerId()));
        methodTable.put(GameMovedToStartingStageEvent.class, x -> gameStage = GameStage.STARTING);
        methodTable.put(GameStartedEvent.class, x -> gameStage = GameStage.INPROGRESS);
        methodTable.put(GameTablesCreatedAndPlayersAssociatedEvent.class,
                x -> tableIdToPlayerIdsMap
                        .putAll(((GameTablesCreatedAndPlayersAssociatedEvent) x)
                                .getTableIdToPlayerIdsMap()));
        methodTable.put(GameFinishedEvent.class, x -> gameStage = GameStage.FINISHED);
        methodTable.put(NewHandIsClearedToStartEvent.class, x -> {});
        methodTable.put(BlindsIncreasedEvent.class, x -> blindSchedule.incrementLevel());
        methodTable.put(TableRemovedEvent.class, x -> {
            var tableId = ((TableRemovedEvent) x).getTableId();
            tableIdToPlayerIdsMap.remove(tableId);
            pausedTablesForBalancing.remove(tableId);
        });
        methodTable.put(TablePausedForBalancingEvent.class, x -> {
            var tableId = ((TablePausedForBalancingEvent) x).getTableId();
            pausedTablesForBalancing.add(tableId);
        });
        methodTable.put(TableResumedAfterBalancingEvent.class, x -> {
            var tableId = ((TableResumedAfterBalancingEvent) x).getTableId();
            pausedTablesForBalancing.remove(tableId);
        });
        methodTable.put(PlayerMovedToNewTableEvent.class, x -> {
            var event = (PlayerMovedToNewTableEvent) x;
            tableIdToPlayerIdsMap.get(event.getFromTableId()).remove(event.getPlayerId());
            tableIdToPlayerIdsMap.get(event.getToTableId()).add(event.getPlayerId());
        });
        methodTable.put(PlayerBustedGameEvent.class, x -> {
            var event = (PlayerBustedGameEvent) x;
            var tableId = tableIdToPlayerIdsMap.entrySet().stream()
                    .filter(y -> y.getValue().contains(event.getPlayerId()))
                    .findAny().get().getKey();
            tableIdToPlayerIdsMap.get(tableId).remove(event.getPlayerId());
        });
    }

    private void applyCommonEvent(GameEvent event) {
        methodTable.get(event.getClass()).applyEvent(event);
        addAppliedEvent(event);
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

    public void attemptToStartNewHand(UUID tableId, Map<UUID, Integer> playerToChipsAtTableMap) {
        if (gameStage != GameStage.INPROGRESS) {
            throw new FlexPokerException("the game must be INPROGRESS if trying"
                    + "to start a new hand");
        }

        playerToChipsAtTableMap.entrySet().stream()
            .filter(x -> x.getValue() == 0)
            .map(x -> x.getKey())
            .forEach(x -> bustPlayer(x));

        var bustedPlayers = tableIdToPlayerIdsMap.get(tableId).stream()
                .filter(x -> !playerToChipsAtTableMap.keySet().contains(x))
                .collect(Collectors.toSet());
        bustedPlayers.forEach(x -> bustPlayer(x));

        if (tableIdToPlayerIdsMap.values().stream().flatMap(Collection::stream).count() == 1) {
            // TODO: do something for the winner
        } else {
            Optional<GameEvent> singleBalancingEvent;
            do {
                singleBalancingEvent = tableBalancer.createSingleBalancingEvent(aggregateVersion + 1,
                        tableId, pausedTablesForBalancing, tableIdToPlayerIdsMap, playerToChipsAtTableMap);
                if (singleBalancingEvent.isPresent()) {
                    aggregateVersion++;
                    addNewEvent(singleBalancingEvent.get());
                    applyCommonEvent(singleBalancingEvent.get());
                }
            } while (singleBalancingEvent.isPresent());

            if (tableIdToPlayerIdsMap.containsKey(tableId) && !pausedTablesForBalancing.contains(tableId)) {
                var event = new NewHandIsClearedToStartEvent(
                        aggregateId, ++aggregateVersion, tableId,
                        blindSchedule.getCurrentBlindAmounts());
                addNewEvent(event);
                applyCommonEvent(event);
            }
        }
    }

    public void increaseBlinds() {
        if (gameStage != GameStage.INPROGRESS) {
            throw new FlexPokerException(
                    "cannot increase blinds if the game isn't in progress");
        }

        if (!blindSchedule.isMaxLevel()) {
            var event = new BlindsIncreasedEvent(aggregateId, ++aggregateVersion);
            addNewEvent(event);
            applyCommonEvent(event);
        }
    }

    private void bustPlayer(UUID playerId) {
        if (tableIdToPlayerIdsMap.values().stream()
                .flatMap(Collection::stream)
                .noneMatch(x -> x.equals(playerId))) {
            throw new FlexPokerException("player is not active in the game");
        }

        var event = new PlayerBustedGameEvent(aggregateId, ++aggregateVersion, playerId);
        addNewEvent(event);
        applyCommonEvent(event);
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

        var event = new GameJoinedEvent(aggregateId, ++aggregateVersion, playerId);
        addNewEvent(event);
        applyCommonEvent(event);
    }

    private void createGameMovedToStartingStageEvent() {
        if (gameStage != GameStage.REGISTERING) {
            throw new FlexPokerException(
                    "to move to STARTING, the game stage must be REGISTERING");
        }
        var event = new GameMovedToStartingStageEvent(aggregateId, ++aggregateVersion);
        addNewEvent(event);
        applyCommonEvent(event);
    }

    private void createGameTablesCreatedAndPlayersAssociatedEvent() {
        if (!tableIdToPlayerIdsMap.isEmpty()) {
            throw new FlexPokerException(
                    "tableToPlayerIdsMap should be empty when initializing the tables");
        }
        var tableIdToPlayerIdsMap = createTableToPlayerMap();

        var event = new GameTablesCreatedAndPlayersAssociatedEvent(
                aggregateId, ++aggregateVersion, tableIdToPlayerIdsMap,
                numberOfPlayersPerTable);
        addNewEvent(event);
        applyCommonEvent(event);
    }

    private Map<UUID, Set<UUID>> createTableToPlayerMap() {
        var randomizedListOfPlayerIds = new ArrayList<>(registeredPlayerIds);
        Collections.shuffle(randomizedListOfPlayerIds);

        var tableIdToPlayerIdsMap = new HashMap<UUID, Set<UUID>>();

        var numberOfTablesToCreate = determineNumberOfTablesToCreate();
        Stream.iterate(0, e -> e)
                .limit(numberOfTablesToCreate)
                .forEach(x -> tableIdToPlayerIdsMap.put(UUID.randomUUID(), new HashSet<>()));

        var tableIdList = new ArrayList<>(tableIdToPlayerIdsMap.keySet());

        Stream.iterate(0, e -> e + 1)
                .limit(randomizedListOfPlayerIds.size())
                .forEach(x -> {
                    var tableIndex = x % tableIdList.size();
                    tableIdToPlayerIdsMap.get(tableIdList.get(tableIndex))
                            .add(randomizedListOfPlayerIds.get(x));
                });

        return tableIdToPlayerIdsMap;
    }

    private int determineNumberOfTablesToCreate() {
        var numberOfTables = registeredPlayerIds.size() / numberOfPlayersPerTable;

        // if the number of people doesn't fit perfectly, then an additional
        // table is needed for the overflow
        if (registeredPlayerIds.size() % numberOfPlayersPerTable != 0) {
            numberOfTables++;
        }

        return numberOfTables;
    }

    private void createGameStartedEvent() {
        if (gameStage != GameStage.STARTING) {
            throw new FlexPokerException(
                    "to move to STARTED, the game stage must be STARTING");
        }

        if (tableIdToPlayerIdsMap.isEmpty()) {
            throw new FlexPokerException(
                    "tableToPlayerIdsMap should be filled at this point");
        }

        var event = new GameStartedEvent(aggregateId, ++aggregateVersion, tableIdToPlayerIdsMap.keySet(), blindSchedule);
        addNewEvent(event);
        applyCommonEvent(event);
    }

}
