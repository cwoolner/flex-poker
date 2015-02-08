package com.flexpoker.table.command.aggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
import com.flexpoker.table.command.events.RiverCardDealtEvent;
import com.flexpoker.table.command.events.TurnCardDealtEvent;
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
            Set<Pot> pots, HandDealerState handDealerState,
            Map<UUID, Integer> chipsInBack, Map<UUID, Integer> chipsInFrontMap,
            Map<UUID, Integer> callAmountsMap, Map<UUID, Integer> raiseToAmountsMap,
            Blinds blinds, Set<UUID> playersToShowCards) {
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
        this.pots = pots;
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

        for (Integer seatPosition : seatMap.keySet()) {
            UUID playerId = seatMap.get(seatPosition);

            if (playerId == null) {
                continue;
            }

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

            if (chipsInFront > chipsInBackMap.get(playerId)) {
                chipsInFrontMap.put(playerId, chipsInBackMap.get(playerId));
            } else {
                chipsInFrontMap.put(playerId, chipsInFront);
            }

            chipsInBackMap.put(playerId,
                    chipsInBackMap.get(playerId) - chipsInFrontMap.get(playerId));

            if (callAmount > chipsInBackMap.get(playerId)) {
                callAmountsMap.put(playerId, chipsInBackMap.get(playerId));
            } else {
                callAmountsMap.put(playerId, callAmount);
            }

            int totalChips = chipsInBackMap.get(playerId) + chipsInFrontMap.get(playerId);

            if (raiseToAmount > totalChips) {
                raiseToAmountsMap.put(playerId, totalChips);
            } else {
                raiseToAmountsMap.put(playerId, raiseToAmount);
            }

            if (raiseToAmountsMap.get(playerId) > 0) {
                Set<PlayerAction> playerActions = possibleSeatActionsMap.get(playerId);
                playerActions.add(PlayerAction.RAISE);
            }

            if (callAmountsMap.get(playerId) > 0) {
                Set<PlayerAction> playerActions = possibleSeatActionsMap.get(playerId);
                playerActions.add(PlayerAction.CALL);
                playerActions.add(PlayerAction.FOLD);
            } else {
                Set<PlayerAction> playerActions = possibleSeatActionsMap.get(playerId);
                playerActions.add(PlayerAction.CHECK);
            }
        }

        lastToActPlayerId = seatMap.get(Integer.valueOf(bigBlindPosition));
        handDealerState = HandDealerState.POCKET_CARDS_DEALT;

        HandDealtEvent handDealtEvent = new HandDealtEvent(tableId, aggregateVersion,
                gameId, entityId, flopCards, turnCard, riverCard, buttonOnPosition,
                smallBlindPosition, bigBlindPosition, lastToActPlayerId, seatMap,
                playerToPocketCardsMap, possibleSeatActionsMap, playersStillInHand,
                handEvaluationList, pots, handDealerState, chipsInBackMap,
                chipsInFrontMap, callAmountsMap, raiseToAmountsMap, blinds,
                playersToShowCards);
        eventsCreated.add(handDealtEvent);

        UUID actionOnPlayerId = seatMap.get(Integer.valueOf(actionOnPosition));

        ActionOnChangedEvent actionOnChangedEvent = new ActionOnChangedEvent(tableId,
                aggregateVersion + 1, gameId, entityId, actionOnPlayerId);
        eventsCreated.add(actionOnChangedEvent);

        return eventsCreated;
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

        if (playerId.equals(lastToActPlayerId)) {
            handleEndOfRound();
        }
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

        if (playerId.equals(lastToActPlayerId)) {
            handleEndOfRound();
        }

        // TODO: This should be a check with all of the tables in the game.
        // TODO: This check should also be done at the beginning of the
        // hand in case the player does not have enough to call the
        // blinds.
        // if (numberOfPlayersLeft == 1) {
        // game.setGameStage(GameStage.FINISHED);
        // }

    }

    void applyEvent(PlayerFoldedEvent event) {
        UUID playerId = event.getPlayerId();

        playersStillInHand.remove(playerId);
        pots.forEach(x -> x.removePlayer(playerId));
        possibleSeatActionsMap.get(playerId).clear();
        callAmountsMap.put(playerId, Integer.valueOf(0));
        raiseToAmountsMap.put(playerId, Integer.valueOf(0));

        if (playersStillInHand.size() == 1) {
            handDealerState = HandDealerState.COMPLETE;
            handleEndOfRound();
        } else if (playerId.equals(lastToActPlayerId)) {
            handleEndOfRound();
        }
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

    private void handleEndOfRound() {
        originatingBettorPlayerId = null;
        moveToNextDealerState();

        // TODO: pot stuff
        // Set<Pot> pots = calculatePotsAfterRoundImplQuery.execute(table);
        // table.getCurrentHand().setPots(pots);

        if (handDealerState == HandDealerState.COMPLETE) {
            determineWinners();

            // TODO: change this to check if a new hand is necessary or if a
            // single person won

        } else {
            resetChipsInFront();
            resetRaiseAmountsAfterRound();
            resetPossibleSeatActionsAfterRound();
        }
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

    private void determineWinners() {
        pots.forEach(pot -> {
            playersStillInHand.forEach(playerInHand -> {
                int numberOfChipsWonForPlayer = pot.getChipsWon(playerInHand);
                addToChipsInBack(playerInHand, numberOfChipsWonForPlayer);

                if (pot.forcePlayerToShowCards(playerInHand)) {
                    playersToShowCards.add(playerInHand);
                }
            });
        });
    }

    private void addToChipsInBack(UUID playerId, int chipsToAdd) {
        int currentAmount = chipsInBackMap.get(playerId).intValue();
        chipsInBackMap.put(playerId, Integer.valueOf(currentAmount + chipsToAdd));
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

    private void moveToNextDealerState() {
        if (handDealerState != HandDealerState.COMPLETE) {
            handDealerState = HandDealerState.values()[handDealerState.ordinal() + 1];
        }
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

}
