package com.flexpoker.table.command.aggregate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.Blinds;
import com.flexpoker.model.HandDealerState;
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.PlayerAction;
import com.flexpoker.model.Pot;
import com.flexpoker.model.card.FlopCards;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.model.card.RiverCard;
import com.flexpoker.model.card.TurnCard;
import com.flexpoker.table.command.events.ActionOnChangedEvent;
import com.flexpoker.table.command.events.FlopCardsDealtEvent;
import com.flexpoker.table.command.events.HandCompletedEvent;
import com.flexpoker.table.command.events.HandDealtEvent;
import com.flexpoker.table.command.events.LastToActChangedEvent;
import com.flexpoker.table.command.events.PlayerCalledEvent;
import com.flexpoker.table.command.events.PlayerCheckedEvent;
import com.flexpoker.table.command.events.PlayerFoldedEvent;
import com.flexpoker.table.command.events.PlayerRaisedEvent;
import com.flexpoker.table.command.events.PotAmountIncreasedEvent;
import com.flexpoker.table.command.events.PotClosedEvent;
import com.flexpoker.table.command.events.PotCreatedEvent;
import com.flexpoker.table.command.events.RiverCardDealtEvent;
import com.flexpoker.table.command.events.RoundCompletedEvent;
import com.flexpoker.table.command.events.TurnCardDealtEvent;
import com.flexpoker.table.command.events.WinnersDeterminedEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class Hand {

    private final UUID gameId;

    private final UUID tableId;

    private final UUID entityId;

    private final Map<Integer, UUID> seatMap;

    private final FlopCards flopCards;

    private final TurnCard turnCard;

    private final RiverCard riverCard;

    private final int buttonOnPosition;

    private final int smallBlindPosition;

    private final int bigBlindPosition;

    private int actionOnPosition;

    private final Map<UUID, PocketCards> playerToPocketCardsMap;

    private final Map<UUID, Set<PlayerAction>> possibleSeatActionsMap;

    private UUID originatingBettorPlayerId;

    private UUID lastToActPlayerId;

    private HandDealerState handDealerState;

    private final List<HandEvaluation> handEvaluationList;

    private final Set<Pot> pots;

    private final Set<UUID> playersStillInHand;

    private final Map<UUID, Integer> chipsInBackMap;

    private final Map<UUID, Integer> chipsInFrontMap;

    private final Map<UUID, Integer> callAmountsMap;

    private final Map<UUID, Integer> raiseToAmountsMap;

    private final Blinds blinds;

    private final Set<UUID> playersToShowCards;

    private boolean flopDealt;

    private boolean turnDealt;

    private boolean riverDealt;

    public Hand(UUID gameId, UUID tableId, UUID entityId, Map<Integer, UUID> seatMap,
            FlopCards flopCards, TurnCard turnCard, RiverCard riverCard,
            int buttonOnPosition, int smallBlindPosition, int bigBlindPosition,
            UUID lastToActPlayerId, Map<UUID, PocketCards> playerToPocketCardsMap,
            Map<UUID, Set<PlayerAction>> possibleSeatActionsMap,
            Set<UUID> playersStillInHand, List<HandEvaluation> handEvaluationList,
            HandDealerState handDealerState, Map<UUID, Integer> chipsInBack,
            Map<UUID, Integer> chipsInFrontMap, Map<UUID, Integer> callAmountsMap,
            Map<UUID, Integer> raiseToAmountsMap, Blinds blinds,
            Set<UUID> playersToShowCards) {
        this.gameId = gameId;
        this.tableId = tableId;
        this.entityId = entityId;
        this.seatMap = seatMap;
        this.flopCards = flopCards;
        this.turnCard = turnCard;
        this.riverCard = riverCard;
        this.buttonOnPosition = buttonOnPosition;
        this.smallBlindPosition = smallBlindPosition;
        this.bigBlindPosition = bigBlindPosition;
        this.lastToActPlayerId = lastToActPlayerId;
        this.playerToPocketCardsMap = playerToPocketCardsMap;
        this.handEvaluationList = handEvaluationList;
        this.possibleSeatActionsMap = possibleSeatActionsMap;
        this.playersStillInHand = playersStillInHand;
        this.pots = new HashSet<>();
        this.handDealerState = handDealerState;
        this.chipsInBackMap = chipsInBack;
        this.chipsInFrontMap = chipsInFrontMap;
        this.callAmountsMap = callAmountsMap;
        this.raiseToAmountsMap = raiseToAmountsMap;
        this.blinds = blinds;
        this.playersToShowCards = playersToShowCards;
    }

    public List<TableEvent> dealHand(int aggregateVersion, int actionOnPosition) {
        List<TableEvent> eventsCreated = new ArrayList<>();

        seatMap.keySet().stream().filter(x -> seatMap.get(x) != null)
                .forEach(seatPosition -> {
                    handleStartOfHandPlayerValues(seatPosition.intValue());
                });

        lastToActPlayerId = seatMap.get(Integer.valueOf(bigBlindPosition));
        handDealerState = HandDealerState.POCKET_CARDS_DEALT;

        HandDealtEvent handDealtEvent = new HandDealtEvent(tableId, aggregateVersion,
                gameId, entityId, flopCards, turnCard, riverCard, buttonOnPosition,
                smallBlindPosition, bigBlindPosition, lastToActPlayerId, seatMap,
                playerToPocketCardsMap, possibleSeatActionsMap, playersStillInHand,
                handEvaluationList, handDealerState, chipsInBackMap, chipsInFrontMap,
                callAmountsMap, raiseToAmountsMap, blinds, playersToShowCards);
        eventsCreated.add(handDealtEvent);

        UUID actionOnPlayerId = seatMap.get(Integer.valueOf(actionOnPosition));

        ActionOnChangedEvent actionOnChangedEvent = new ActionOnChangedEvent(tableId,
                aggregateVersion + 1, gameId, entityId, actionOnPlayerId);
        eventsCreated.add(actionOnChangedEvent);

        return eventsCreated;
    }

    private void handleStartOfHandPlayerValues(int seatPosition) {
        UUID playerId = seatMap.get(Integer.valueOf(seatPosition));

        int chipsInFront = 0;
        int callAmount = blinds.getBigBlind();
        int raiseToAmount = blinds.getBigBlind() * 2;

        if (seatPosition == bigBlindPosition) {
            chipsInFront = blinds.getBigBlind();
            callAmount = 0;
        } else if (seatPosition == smallBlindPosition) {
            chipsInFront = blinds.getSmallBlind();
            callAmount = blinds.getSmallBlind();
        }

        if (chipsInFront > chipsInBackMap.get(playerId).intValue()) {
            chipsInFrontMap.put(playerId, chipsInBackMap.get(playerId));
        } else {
            chipsInFrontMap.put(playerId, Integer.valueOf(chipsInFront));
        }

        subtractFromChipsInBack(playerId, chipsInFrontMap.get(playerId).intValue());

        if (callAmount > chipsInBackMap.get(playerId).intValue()) {
            callAmountsMap.put(playerId, chipsInBackMap.get(playerId));
        } else {
            callAmountsMap.put(playerId, Integer.valueOf(callAmount));
        }

        int totalChips = chipsInBackMap.get(playerId).intValue()
                + chipsInFrontMap.get(playerId).intValue();

        if (raiseToAmount > totalChips) {
            raiseToAmountsMap.put(playerId, Integer.valueOf(totalChips));
        } else {
            raiseToAmountsMap.put(playerId, Integer.valueOf(raiseToAmount));
        }

        if (raiseToAmountsMap.get(playerId).intValue() > 0) {
            Set<PlayerAction> playerActions = possibleSeatActionsMap.get(playerId);
            playerActions.add(PlayerAction.RAISE);
        }

        if (callAmountsMap.get(playerId).intValue() > 0) {
            Set<PlayerAction> playerActions = possibleSeatActionsMap.get(playerId);
            playerActions.add(PlayerAction.CALL);
            playerActions.add(PlayerAction.FOLD);
        } else {
            Set<PlayerAction> playerActions = possibleSeatActionsMap.get(playerId);
            playerActions.add(PlayerAction.CHECK);
        }
    }

    public PlayerCheckedEvent check(UUID playerId, int aggregateVersion) {
        checkActionOnPlayer(playerId);
        checkPerformAction(playerId, PlayerAction.CHECK);

        PlayerCheckedEvent playerCheckedEvent = new PlayerCheckedEvent(tableId,
                aggregateVersion, gameId, entityId, playerId);
        return playerCheckedEvent;
    }

    public PlayerCalledEvent call(UUID playerId, int aggregateVersion) {
        checkActionOnPlayer(playerId);
        checkPerformAction(playerId, PlayerAction.CALL);

        PlayerCalledEvent playerCalledEvent = new PlayerCalledEvent(tableId,
                aggregateVersion, gameId, entityId, playerId);
        return playerCalledEvent;
    }

    public PlayerFoldedEvent fold(UUID playerId, int aggregateVersion) {
        checkActionOnPlayer(playerId);
        checkPerformAction(playerId, PlayerAction.FOLD);

        PlayerFoldedEvent playerFoldedEvent = new PlayerFoldedEvent(tableId,
                aggregateVersion, gameId, entityId, playerId);
        return playerFoldedEvent;
    }

    public PlayerRaisedEvent raise(UUID playerId, int aggregateVersion, int raiseToAmount) {
        checkActionOnPlayer(playerId);
        checkPerformAction(playerId, PlayerAction.RAISE);
        checkRaiseAmountValue(playerId, raiseToAmount);

        PlayerRaisedEvent playerRaisedEvent = new PlayerRaisedEvent(tableId,
                aggregateVersion, gameId, entityId, playerId, raiseToAmount);
        return playerRaisedEvent;
    }

    TableEvent expireActionOn(UUID playerId, int aggregateVersion) {
        if (callAmountsMap.get(playerId).intValue() == 0) {
            return check(playerId, aggregateVersion);
        }

        return fold(playerId, aggregateVersion);
    }

    public List<TableEvent> changeActionOn(int aggregateVersion) {
        List<TableEvent> eventsCreated = new ArrayList<>();

        // do not change action on if the hand is over. starting a new hand
        // should adjust that
        if (handDealerState == HandDealerState.COMPLETE) {
            return eventsCreated;
        }

        UUID actionOnPlayerId = seatMap.get(Integer.valueOf(actionOnPosition));

        // the player just bet, so a new last to act needs to be determined
        if (actionOnPlayerId.equals(originatingBettorPlayerId)) {
            UUID nextPlayerToAct = findNextToAct();
            UUID lastPlayerToAct = seatMap.get(Integer.valueOf(determineLastToAct()));

            ActionOnChangedEvent actionOnChangedEvent = new ActionOnChangedEvent(tableId,
                    aggregateVersion, gameId, entityId, nextPlayerToAct);
            LastToActChangedEvent lastToActChangedEvent = new LastToActChangedEvent(
                    tableId, aggregateVersion + 1, gameId, entityId, lastPlayerToAct);

            eventsCreated.add(actionOnChangedEvent);
            eventsCreated.add(lastToActChangedEvent);

        }
        // if it's the last player to act, recalculate for a new round
        else if (actionOnPlayerId.equals(lastToActPlayerId)) {
            UUID nextPlayerToAct = findActionOnPlayerIdForNewRound();
            UUID newRoundLastPlayerToAct = seatMap.get(Integer
                    .valueOf(determineLastToAct()));

            ActionOnChangedEvent actionOnChangedEvent = new ActionOnChangedEvent(tableId,
                    aggregateVersion, gameId, entityId, nextPlayerToAct);
            LastToActChangedEvent lastToActChangedEvent = new LastToActChangedEvent(
                    tableId, aggregateVersion + 1, gameId, entityId,
                    newRoundLastPlayerToAct);
            eventsCreated.add(actionOnChangedEvent);
            eventsCreated.add(lastToActChangedEvent);
        }
        // just a normal transition mid-round
        else {
            UUID nextPlayerToAct = findNextToAct();
            ActionOnChangedEvent actionOnChangedEvent = new ActionOnChangedEvent(tableId,
                    aggregateVersion, gameId, entityId, nextPlayerToAct);
            eventsCreated.add(actionOnChangedEvent);
        }

        return eventsCreated;
    }

    Optional<TableEvent> dealCommonCardsIfAppropriate(int aggregateVersion) {
        if (handDealerState == HandDealerState.FLOP_DEALT && !flopDealt) {
            return Optional.of(new FlopCardsDealtEvent(tableId, aggregateVersion, gameId,
                    entityId));
        }

        if (handDealerState == HandDealerState.TURN_DEALT && !turnDealt) {
            return Optional.of(new TurnCardDealtEvent(tableId, aggregateVersion, gameId,
                    entityId));
        }

        if (handDealerState == HandDealerState.RIVER_DEALT && !riverDealt) {
            return Optional.of(new RiverCardDealtEvent(tableId, aggregateVersion, gameId,
                    entityId));
        }

        return Optional.empty();
    }

    List<TableEvent> handlePotAndRoundCompleted(int aggregateVersion) {
        if (!seatMap.get(Integer.valueOf(actionOnPosition)).equals(lastToActPlayerId)
                && playersStillInHand.size() > 1) {
            return Collections.emptyList();
        }

        List<TableEvent> tableEvents = new ArrayList<>();
        tableEvents.addAll(calculatePots(aggregateVersion));

        HandDealerState nextHandDealerState = playersStillInHand.size() == 1 ? HandDealerState.COMPLETE
                : HandDealerState.values()[handDealerState.ordinal() + 1];
        tableEvents.add(new RoundCompletedEvent(tableId, aggregateVersion
                + tableEvents.size(), gameId, entityId, nextHandDealerState));

        return tableEvents;
    }

    Optional<TableEvent> finishHandIfAppropriate(int aggregateVersion) {
        if (handDealerState == HandDealerState.COMPLETE) {
            return Optional.of(new HandCompletedEvent(tableId, aggregateVersion, gameId,
                    entityId));
        }

        return Optional.empty();
    }

    private void checkRaiseAmountValue(UUID playerId, int raiseToAmount) {
        int playersTotalChips = chipsInBackMap.get(playerId).intValue()
                + chipsInFrontMap.get(playerId).intValue();
        if (raiseToAmount < raiseToAmountsMap.get(playerId).intValue()
                || raiseToAmount > playersTotalChips) {
            throw new IllegalArgumentException("Raise amount must be between the "
                    + "minimum and maximum values.");
        }
    }

    private void checkActionOnPlayer(UUID playerId) {
        UUID actionOnPlayerId = seatMap.get(Integer.valueOf(actionOnPosition));
        if (!playerId.equals(actionOnPlayerId)) {
            throw new FlexPokerException("action is not on the player attempting action");
        }
    }

    private void checkPerformAction(UUID playerId, PlayerAction playerAction) {
        if (!possibleSeatActionsMap.get(playerId).contains(playerAction)) {
            throw new FlexPokerException("not allowed to " + playerAction);
        }
    }

    void applyEvent(PlayerCheckedEvent event) {
        UUID playerId = event.getPlayerId();
        possibleSeatActionsMap.get(playerId).clear();
        callAmountsMap.put(playerId, Integer.valueOf(0));
        raiseToAmountsMap.put(playerId, Integer.valueOf(0));
    }

    void applyEvent(PlayerCalledEvent event) {
        UUID playerId = event.getPlayerId();
        possibleSeatActionsMap.get(playerId).clear();
        callAmountsMap.put(playerId, Integer.valueOf(0));
        raiseToAmountsMap.put(playerId, Integer.valueOf(0));

        int newChipsInFront = chipsInFrontMap.get(playerId).intValue()
                + callAmountsMap.get(playerId).intValue();
        chipsInFrontMap.put(playerId, Integer.valueOf(newChipsInFront));

        int newChipsInBack = chipsInBackMap.get(playerId).intValue()
                - callAmountsMap.get(playerId).intValue();
        chipsInBackMap.put(playerId, Integer.valueOf(newChipsInBack));
    }

    void applyEvent(PlayerFoldedEvent event) {
        UUID playerId = event.getPlayerId();
        playersStillInHand.remove(playerId);
        pots.forEach(x -> x.removePlayer(playerId));
        possibleSeatActionsMap.get(playerId).clear();
        callAmountsMap.put(playerId, Integer.valueOf(0));
        raiseToAmountsMap.put(playerId, Integer.valueOf(0));
    }

    void applyEvent(PlayerRaisedEvent event) {
        UUID playerId = event.getPlayerId();
        int raiseToAmount = event.getRaiseToAmount();

        originatingBettorPlayerId = playerId;

        int raiseAboveCall = raiseToAmount
                - (chipsInFrontMap.get(playerId).intValue() + callAmountsMap
                        .get(playerId).intValue());
        int increaseOfChipsInFront = raiseToAmount
                - chipsInFrontMap.get(playerId).intValue();

        playersStillInHand.stream().filter(x -> !x.equals(playerId)).forEach(x -> {
            adjustPlayersFieldsAfterRaise(raiseToAmount, raiseAboveCall, x);
        });

        possibleSeatActionsMap.get(playerId).clear();
        callAmountsMap.put(playerId, Integer.valueOf(0));
        raiseToAmountsMap.put(playerId, Integer.valueOf(blinds.getBigBlind()));

        chipsInFrontMap.put(playerId, Integer.valueOf(raiseToAmount));

        int newChipsInBack = chipsInBackMap.get(playerId).intValue()
                - increaseOfChipsInFront;
        chipsInBackMap.put(playerId, Integer.valueOf(newChipsInBack));
    }

    void applyEvent(ActionOnChangedEvent event) {
        actionOnPosition = seatMap.entrySet().stream()
                .filter(x -> x.getValue().equals(event.getPlayerId())).findAny().get()
                .getKey().intValue();
    }

    void applyEvent(LastToActChangedEvent event) {
        lastToActPlayerId = event.getPlayerId();
    }

    void applyEvent(@SuppressWarnings("unused") FlopCardsDealtEvent event) {
        flopDealt = true;
    }

    void applyEvent(@SuppressWarnings("unused") TurnCardDealtEvent event) {
        turnDealt = true;
    }

    void applyEvent(@SuppressWarnings("unused") RiverCardDealtEvent event) {
        riverDealt = true;
    }

    /**
     * The general approach to calculating pots is as follows:
     * 
     * 1. Discover all of the distinct numbers of chips in front of each player.
     * For example, if everyone has 30 chips in front, 30 would be the only
     * number in the distinct set. If two players had 10 and one person had 20,
     * then 10 and 20 would be in the set.
     * 
     * 2. Loop through each chip count, starting with the smallest, and shave
     * off the number of chips from each stack in front of each player, and
     * place them into an open pot.
     * 
     * 3. If an open pot does not exist, create a new one.
     * 
     * 4. If it's determined that a player is all-in, then the pot for that
     * player's all-in should be closed. Multiple closed pots can exist, but
     * only one open pot should ever exist at any given time.
     * 
     * @param aggregateVersion
     */
    private List<TableEvent> calculatePots(int aggregateVersion) {
        List<TableEvent> newPotEvents = new ArrayList<>();

        List<Integer> distinctChipsInFrontAmountsList = chipsInFrontMap.values().stream()
                .filter(x -> x.intValue() != 0).distinct().collect(Collectors.toList());
        distinctChipsInFrontAmountsList.sort(null);

        distinctChipsInFrontAmountsList
                .forEach(chipsPerLevel -> {
                    Optional<Pot> openPotOptional = pots.stream().filter(x -> x.isOpen())
                            .findAny();

                    Pot openPot;

                    if (openPotOptional.isPresent()) {
                        openPot = openPotOptional.get();
                    } else {
                        openPot = new Pot(UUID.randomUUID(),
                                handEvaluationList
                                        .stream()
                                        .filter(x -> playersStillInHand.contains(x
                                                .getPlayerId()))
                                        .collect(Collectors.toSet()));
                        PotCreatedEvent potCreatedEvent = new PotCreatedEvent(tableId,
                                aggregateVersion, gameId, entityId, openPot.getId(),
                                playersStillInHand
                                        .stream()
                                        .filter(x -> chipsInFrontMap.getOrDefault(x,
                                                Integer.valueOf(0)).intValue() > 0)
                                        .collect(Collectors.toSet()));
                        newPotEvents.add(potCreatedEvent);
                        applyEvent(potCreatedEvent);
                    }

                    PotAmountIncreasedEvent potAmountIncreasedEvent = new PotAmountIncreasedEvent(
                            tableId, aggregateVersion + newPotEvents.size(), gameId,
                            entityId, openPot.getId(), chipsPerLevel.intValue());
                    newPotEvents.add(potAmountIncreasedEvent);
                    applyEvent(potAmountIncreasedEvent);

                    seatMap.values()
                            .forEach(
                                    playerInSeat -> {
                                        if (chipsInFrontMap.getOrDefault(playerInSeat,
                                                Integer.valueOf(0)).intValue() > 0) {

                                            if (playerIsAllIn(playerInSeat)) {
                                                PotClosedEvent potClosedEvent = new PotClosedEvent(
                                                        tableId, aggregateVersion,
                                                        gameId, entityId, openPot.getId());
                                                newPotEvents.add(potClosedEvent);
                                                applyEvent(potClosedEvent);
                                            }
                                        }
                                    });
                });
        return newPotEvents;
    }

    private boolean playerIsAllIn(UUID playerInSeat) {
        return chipsInBackMap.getOrDefault(playerInSeat, Integer.valueOf(0)).intValue() == 0;
    }

    private UUID findActionOnPlayerIdForNewRound() {
        int buttonIndex = buttonOnPosition;

        for (int i = buttonIndex + 1; i < seatMap.size(); i++) {
            UUID playerAtTable = seatMap.get(Integer.valueOf(i));
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)
                    && chipsInBackMap.get(playerAtTable).intValue() != 0) {
                return playerAtTable;
            }
        }

        for (int i = 0; i < buttonIndex; i++) {
            UUID playerAtTable = seatMap.get(Integer.valueOf(i));
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)
                    && chipsInBackMap.get(playerAtTable).intValue() != 0) {
                return playerAtTable;
            }
        }

        throw new FlexPokerException("couldn't determine new action on after round");
    }

    private void resetChipsInFront() {
        Set<UUID> playersInMap = chipsInFrontMap.keySet();
        playersInMap.forEach(x -> chipsInFrontMap.put(x, Integer.valueOf(0)));
    }

    private void resetCallAmounts() {
        Set<UUID> playersInMap = callAmountsMap.keySet();
        playersInMap.forEach(x -> callAmountsMap.put(x, Integer.valueOf(0)));
    }

    private void resetRaiseTo() {
        Set<UUID> playersInMap = raiseToAmountsMap.keySet();
        playersInMap.forEach(x -> raiseToAmountsMap.put(x, Integer.valueOf(0)));
    }

    private void resetRaiseAmountsAfterRound() {
        playersStillInHand
                .forEach(playerInHand -> {
                    callAmountsMap.put(playerInHand, Integer.valueOf(0));
                    if (blinds.getBigBlind() > chipsInBackMap.get(playerInHand)
                            .intValue()) {
                        raiseToAmountsMap.put(playerInHand,
                                chipsInBackMap.get(playerInHand));
                    } else {
                        raiseToAmountsMap.put(playerInHand,
                                Integer.valueOf(blinds.getBigBlind()));
                    }
                });
    }

    private void resetPossibleSeatActionsAfterRound() {
        playersStillInHand.forEach(playerInHand -> {
            possibleSeatActionsMap.get(playerInHand).clear();
            possibleSeatActionsMap.get(playerInHand).add(PlayerAction.CHECK);
            possibleSeatActionsMap.get(playerInHand).add(PlayerAction.RAISE);
        });
    }

    private UUID findNextToAct() {
        for (int i = actionOnPosition + 1; i < seatMap.size(); i++) {
            UUID playerAtTable = seatMap.get(Integer.valueOf(i));
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)) {
                return playerAtTable;
            }
        }

        for (int i = 0; i < actionOnPosition; i++) {
            UUID playerAtTable = seatMap.get(Integer.valueOf(i));
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)) {
                return playerAtTable;
            }
        }

        throw new IllegalStateException("unable to find next to act");
    }

    Optional<WinnersDeterminedEvent> determineWinnersIfAppropriate(int aggregateVersion) {
        if (handDealerState == HandDealerState.COMPLETE) {
            Set<UUID> playersToShowCards = new HashSet<>();
            Map<UUID, Integer> playersToChipsWonMap = new HashMap<>();

            pots.forEach(pot -> {
                playersStillInHand.forEach(playerInHand -> {
                    int numberOfChipsWonForPlayer = pot.getChipsWon(playerInHand);
                    playersToChipsWonMap.put(playerInHand,
                            Integer.valueOf(numberOfChipsWonForPlayer));

                    if (pot.forcePlayerToShowCards(playerInHand)) {
                        playersToShowCards.add(playerInHand);
                    }
                });
            });

            return Optional.of(new WinnersDeterminedEvent(tableId, aggregateVersion,
                    gameId, entityId, playersToShowCards, playersToChipsWonMap));
        }

        return Optional.empty();
    }

    private void addToChipsInBack(UUID playerId, int chipsToAdd) {
        int currentAmount = chipsInBackMap.get(playerId).intValue();
        chipsInBackMap.put(playerId, Integer.valueOf(currentAmount + chipsToAdd));
    }

    private void subtractFromChipsInBack(UUID playerId, int chipsToSubtract) {
        int currentAmount = chipsInBackMap.get(playerId).intValue();
        chipsInBackMap.put(playerId, Integer.valueOf(currentAmount - chipsToSubtract));
    }

    private void subtractFromChipsInFront(UUID playerId, int chipsToSubtract) {
        int currentAmount = chipsInFrontMap.get(playerId).intValue();
        chipsInFrontMap.put(playerId, Integer.valueOf(currentAmount - chipsToSubtract));
    }

    private int determineLastToAct() {
        int seatIndex;

        if (originatingBettorPlayerId == null) {
            seatIndex = buttonOnPosition;
        } else {
            seatIndex = seatMap.entrySet().stream()
                    .filter(x -> x.getValue().equals(originatingBettorPlayerId))
                    .findAny().get().getKey().intValue();

            if (seatIndex == 0) {
                seatIndex = seatMap.size() - 1;
            } else {
                seatIndex--;
            }
        }

        for (int i = seatIndex; i >= 0; i--) {
            UUID playerAtTable = seatMap.get(Integer.valueOf(i));
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)) {
                return i;
            }
        }

        for (int i = seatMap.size() - 1; i > seatIndex; i--) {
            UUID playerAtTable = seatMap.get(Integer.valueOf(i));
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)) {
                return i;
            }
        }

        throw new IllegalStateException("unable to determine last to act");
    }

    boolean idMatches(UUID handId) {
        return entityId.equals(handId);
    }

    private void adjustPlayersFieldsAfterRaise(int raiseToAmount, int raiseAboveCall,
            UUID playerId) {
        possibleSeatActionsMap.get(playerId).clear();
        possibleSeatActionsMap.get(playerId).add(PlayerAction.CALL);
        possibleSeatActionsMap.get(playerId).add(PlayerAction.FOLD);

        int totalChips = chipsInBackMap.get(playerId).intValue()
                + chipsInFrontMap.get(playerId).intValue();

        if (totalChips <= raiseToAmount) {
            callAmountsMap.put(playerId, Integer.valueOf(totalChips));
            raiseToAmountsMap.put(playerId, Integer.valueOf(0));
        } else {
            callAmountsMap.put(
                    playerId,
                    Integer.valueOf(raiseToAmount
                            - chipsInFrontMap.get(playerId).intValue()));
            possibleSeatActionsMap.get(playerId).add(PlayerAction.RAISE);
            if (totalChips < raiseToAmount + raiseAboveCall) {
                raiseToAmountsMap.put(playerId, Integer.valueOf(totalChips));
            } else {
                raiseToAmountsMap.put(playerId,
                        Integer.valueOf(raiseToAmount + raiseAboveCall));
            }
        }
    }

    void applyEvent(PotAmountIncreasedEvent event) {
        Pot pot = fetchPotById(event.getPotId());
        pot.addChips(event.getAmountIncreased());
        playersStillInHand.forEach(x -> subtractFromChipsInFront(x,
                event.getAmountIncreased()));
    }

    void applyEvent(PotClosedEvent event) {
        Pot pot = fetchPotById(event.getPotId());
        pot.closePot();
    }

    void applyEvent(PotCreatedEvent event) {
        Set<HandEvaluation> handEvaluationsOfPlayersInPot = handEvaluationList.stream()
                .filter(x -> event.getPlayersInvolved().contains(x.getPlayerId()))
                .collect(Collectors.toSet());
        pots.add(new Pot(event.getPotId(), handEvaluationsOfPlayersInPot));
    }

    void applyEvent(RoundCompletedEvent event) {
        handDealerState = event.getNextHandDealerState();
        originatingBettorPlayerId = null;
        resetChipsInFront();
        resetRaiseAmountsAfterRound();
        resetPossibleSeatActionsAfterRound();
    }

    void applyEvent(WinnersDeterminedEvent event) {
        playersToShowCards.addAll(event.getPlayersToShowCards());
        event.getPlayersToChipsWonMap().forEach((playerId, chips) -> {
            addToChipsInBack(playerId, chips.intValue());
        });
    }

    private Pot fetchPotById(UUID potId) {
        return pots.stream().filter(x -> x.idMatches(potId)).findAny().get();
    }

}
