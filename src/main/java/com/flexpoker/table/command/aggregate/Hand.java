package com.flexpoker.table.command.aggregate;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.UUID;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.Blinds;
import com.flexpoker.model.HandDealerState;
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.HandRoundState;
import com.flexpoker.model.PlayerAction;
import com.flexpoker.model.Pot;
import com.flexpoker.model.card.FlopCards;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.model.card.RiverCard;
import com.flexpoker.model.card.TurnCard;
import com.flexpoker.table.command.events.HandDealtEvent;
import com.flexpoker.table.command.events.PlayerCalledEvent;
import com.flexpoker.table.command.events.PlayerCheckedEvent;
import com.flexpoker.table.command.events.PlayerFoldedEvent;
import com.flexpoker.table.command.events.PlayerRaisedEvent;
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

    private UUID nextToActPlayerId;

    private HandDealerState handDealerState;

    private HandRoundState handRoundState;

    private final List<HandEvaluation> handEvaluationList;

    private final Set<Pot> pots;

    private final Set<UUID> playersStillInHand;

    private Timer actionOnTimer;

    private final Map<UUID, Integer> chipsInBackMap;

    private final Map<UUID, Integer> chipsInFrontMap;

    private final Map<UUID, Integer> callAmountsMap;

    private final Map<UUID, Integer> raiseToAmountsMap;

    private final Blinds blinds;

    private final Set<UUID> playersToShowCardsMap;

    public Hand(UUID gameId, UUID tableId, UUID entityId, Map<Integer, UUID> seatMap,
            FlopCards flopCards, TurnCard turnCard, RiverCard riverCard,
            int buttonOnPosition, int smallBlindPosition, int bigBlindPosition,
            int actionOnPosition, Map<UUID, PocketCards> playerToPocketCardsMap,
            Map<UUID, Set<PlayerAction>> possibleSeatActionsMap,
            Set<UUID> playersStillInHand, List<HandEvaluation> handEvaluationList,
            Set<Pot> pots, HandDealerState handDealerState,
            HandRoundState handRoundState, Map<UUID, Integer> chipsInBack,
            Map<UUID, Integer> chipsInFrontMap, Map<UUID, Integer> callAmountsMap,
            Map<UUID, Integer> raiseToAmountsMap, Blinds blinds,
            Set<UUID> playersToShowCardsMap) {
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
        this.actionOnPosition = actionOnPosition;
        this.playerToPocketCardsMap = playerToPocketCardsMap;
        this.handEvaluationList = handEvaluationList;
        this.possibleSeatActionsMap = possibleSeatActionsMap;
        this.playersStillInHand = playersStillInHand;
        this.pots = pots;
        this.handDealerState = handDealerState;
        this.handRoundState = handRoundState;
        this.chipsInBackMap = chipsInBack;
        this.chipsInFrontMap = chipsInFrontMap;
        this.callAmountsMap = callAmountsMap;
        this.raiseToAmountsMap = raiseToAmountsMap;
        this.blinds = blinds;
        this.playersToShowCardsMap = playersToShowCardsMap;
    }

    public HandDealtEvent dealHand(int aggregateVersion) {
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
        handRoundState = HandRoundState.ROUND_IN_PROGRESS;

        // TODO: set action on timer

        HandDealtEvent handDealtEvent = new HandDealtEvent(tableId, aggregateVersion,
                gameId, entityId, flopCards, turnCard, riverCard, buttonOnPosition,
                smallBlindPosition, bigBlindPosition, actionOnPosition, seatMap,
                playerToPocketCardsMap, possibleSeatActionsMap, playersStillInHand,
                handEvaluationList, pots, handDealerState, handRoundState,
                chipsInBackMap, chipsInFrontMap, callAmountsMap, raiseToAmountsMap,
                blinds, playersToShowCardsMap);
        return handDealtEvent;
    }

    public List<TableEvent> check(UUID playerId, int aggregateVersion) {
        checkActionOnPlayer(playerId);
        checkPerformAction(playerId, PlayerAction.CHECK);

        PlayerCheckedEvent playerCheckedEvent = new PlayerCheckedEvent(tableId,
                aggregateVersion, gameId, entityId, playerId);
        return Arrays.asList(new TableEvent[] { playerCheckedEvent });
    }

    public List<TableEvent> call(UUID playerId, int aggregateVersion) {
        checkActionOnPlayer(playerId);
        checkPerformAction(playerId, PlayerAction.CALL);

        PlayerCalledEvent playerCalledEvent = new PlayerCalledEvent(tableId,
                aggregateVersion, gameId, entityId, playerId);
        return Arrays.asList(new TableEvent[] { playerCalledEvent });
    }

    public List<TableEvent> fold(UUID playerId, int aggregateVersion) {
        checkActionOnPlayer(playerId);
        checkPerformAction(playerId, PlayerAction.FOLD);

        PlayerFoldedEvent playerFoldedEvent = new PlayerFoldedEvent(tableId,
                aggregateVersion, gameId, entityId, playerId);
        return Arrays.asList(new TableEvent[] { playerFoldedEvent });
    }

    public List<TableEvent> raise(UUID playerId, int aggregateVersion, int raiseToAmount) {
        checkActionOnPlayer(playerId);
        checkPerformAction(playerId, PlayerAction.RAISE);
        checkRaiseAmountValue(playerId, raiseToAmount);

        PlayerRaisedEvent playerRaisedEvent = new PlayerRaisedEvent(tableId,
                aggregateVersion, gameId, entityId, playerId, raiseToAmount);
        return Arrays.asList(new TableEvent[] { playerRaisedEvent });
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
        callAmountsMap.put(playerId, 0);
        raiseToAmountsMap.put(playerId, 0);

        if (playerId.equals(lastToActPlayerId)) {
            handleEndOfRound();
        } else {
            handleMiddleOfRound(playerId);
        }
    }

    void applyEvent(PlayerCalledEvent event) {
        UUID playerId = event.getPlayerId();

        possibleSeatActionsMap.get(playerId).clear();
        callAmountsMap.put(playerId, 0);
        raiseToAmountsMap.put(playerId, 0);

        int newChipsInFront = chipsInFrontMap.get(playerId)
                + callAmountsMap.get(playerId);
        chipsInFrontMap.put(playerId, newChipsInFront);

        int newChipsInBack = chipsInBackMap.get(playerId) - callAmountsMap.get(playerId);
        chipsInBackMap.put(playerId, newChipsInBack);

        if (playerId.equals(lastToActPlayerId)) {
            handleEndOfRound();
        } else {
            handleMiddleOfRound(playerId);
        }

        // int numberOfPlayersLeft = 0;
        // for (Seat seat : table.getSeats()) {
        // if (seat.getUserGameStatus() != null
        // && seat.getUserGameStatus().getChips() != 0) {
        // numberOfPlayersLeft++;
        // }
        // }

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
        pots.forEach(x -> x.removeSeat(playerId));
        possibleSeatActionsMap.get(playerId).clear();
        callAmountsMap.put(playerId, 0);
        raiseToAmountsMap.put(playerId, 0);

        if (playersStillInHand.size() == 1) {
            handDealerState = HandDealerState.COMPLETE;
            handleEndOfRound();
        } else if (playerId.equals(lastToActPlayerId)) {
            handleEndOfRound();
        } else {
            handleMiddleOfRound(playerId);
        }
    }

    void applyEvent(PlayerRaisedEvent event) {
        UUID playerId = event.getPlayerId();

        playersStillInHand.remove(playerId);
        pots.forEach(x -> x.removeSeat(playerId));
        possibleSeatActionsMap.get(playerId).clear();
        callAmountsMap.put(playerId, 0);
        raiseToAmountsMap.put(playerId, 0);

        if (playersStillInHand.size() == 1) {
            handDealerState = HandDealerState.COMPLETE;
            handleEndOfRound();
        } else if (playerId.equals(lastToActPlayerId)) {
            handleEndOfRound();
        } else {
            handleMiddleOfRound(playerId);
        }
    }

    private void handleMiddleOfRound(UUID playerId) {
        handRoundState = HandRoundState.ROUND_IN_PROGRESS;
        actionOnPosition = findNextToAct();

        // TODO: add the timer
        // Timer actionOnTimer =
        // scheduleAndReturnActionOnTimerCommand.execute(game, table,
        // nextToActSeat);
        // nextToActSeat.setActionOnTimer(actionOnTimer);
    }

    private void handleEndOfRound() {
        originatingBettorPlayerId = null;
        handRoundState = HandRoundState.ROUND_COMPLETE;
        moveToNextDealerState();

        // TODO: pot stuff
        // Set<Pot> pots = calculatePotsAfterRoundImplQuery.execute(table);
        // table.getCurrentHand().setPots(pots);

        if (handDealerState == HandDealerState.COMPLETE) {
            // TODO: all of this stuff is probably unnecessary since a new hand
            // will reset all of this anyway
            resetChipsInFront();
            resetCallAmounts();
            resetRaiseTo();
            determineWinners();

            // TODO: change this to check if a new hand is necessary or if a
            // single person won
            // TODO: add the show cards map
            // table.resetShowCards();

            // TODO: this needs to be done at the table level
            // assignNewHandBigBlind(table);
            // assignNewHandSmallBlind(table);
            // assignNewHandButton(table);
            // assignNewHandActionOn(game, table);

            // TODO: can't send command from here
            // startNewHandCommand.execute(game, table);
        } else {
            resetChipsInFront();
            determineActionOnForNewRound();
            lastToActPlayerId = seatMap.get(Integer.valueOf(determineLastToAct()));
            resetRaiseAmountsAfterRound();
            resetPossibleSeatActionsAfterRound();
        }
    }

    private void determineActionOnForNewRound() {
        int buttonIndex = buttonOnPosition;

        for (int i = buttonIndex + 1; i < seatMap.size(); i++) {
            UUID playerAtTable = seatMap.get(Integer.valueOf(i));
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)
                    && chipsInBackMap.get(playerAtTable) != 0) {
                actionOnPosition = i;

                // TODO: timer
                // Timer actionOnTimer =
                // createAndStartActionOnTimerCommand.execute(
                // game, table, newActionOnSeat);
                // newActionOnSeat.setActionOnTimer(actionOnTimer);
            }
        }

        for (int i = 0; i < buttonIndex; i++) {
            UUID playerAtTable = seatMap.get(Integer.valueOf(i));
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)
                    && chipsInBackMap.get(playerAtTable) != 0) {
                actionOnPosition = i;

                // TODO: timer
                // Timer actionOnTimer =
                // createAndStartActionOnTimerCommand.execute(
                // game, table, newActionOnSeat);
                // newActionOnSeat.setActionOnTimer(actionOnTimer);
            }
        }

        throw new FlexPokerException("couldn't determine new action on after round");
    }

    private void resetChipsInFront() {
        Set<UUID> playersInMap = chipsInFrontMap.keySet();
        playersInMap.forEach(x -> chipsInFrontMap.put(x, 0));
    }

    private void resetCallAmounts() {
        Set<UUID> playersInMap = callAmountsMap.keySet();
        playersInMap.forEach(x -> callAmountsMap.put(x, 0));
    }

    private void resetRaiseTo() {
        Set<UUID> playersInMap = raiseToAmountsMap.keySet();
        playersInMap.forEach(x -> raiseToAmountsMap.put(x, 0));
    }

    private void resetRaiseAmountsAfterRound() {
        playersStillInHand.forEach(playerInHand -> {
            callAmountsMap.put(playerInHand, 0);
            if (blinds.getBigBlind() > chipsInBackMap.get(playerInHand)) {
                raiseToAmountsMap.put(playerInHand, chipsInBackMap.get(playerInHand));
            } else {
                raiseToAmountsMap.put(playerInHand, blinds.getBigBlind());
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

    private int findNextToAct() {
        for (int i = actionOnPosition + 1; i < seatMap.size(); i++) {
            UUID playerAtTable = seatMap.get(Integer.valueOf(i));
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)) {
                return i;
            }
        }

        for (int i = 0; i < actionOnPosition; i++) {
            UUID playerAtTable = seatMap.get(Integer.valueOf(i));
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)) {
                return i;
            }
        }

        throw new IllegalStateException("unable to find next to act");
    }

    private void determineWinners() {
        for (Pot pot : pots) {
            // TODO: commented-out because pot changed to UUID
            Set<UUID> winners = new HashSet<>();
            // Set<Seat> winners = determinePotWinnersImplQuery.execute(table,
            // pot.getSeats(), handEvaluationList);

            pot.addWinners(winners);

            int numberOfWinners = winners.size();
            int numberOfChips = pot.getAmount() / numberOfWinners;
            int bonusChips = pot.getAmount() % numberOfWinners;
            int numberOfPlayersInPot = pot.getSeats().size();

            if (bonusChips > 0) {
                // TODO: randomize this (or maybe being in a Set is good enough)
                UUID winnerOfBonusChips = winners.stream().findFirst().get();
                addToChipsInBack(winnerOfBonusChips, bonusChips);
            }

            for (UUID winner : winners) {
                addToChipsInBack(winner, numberOfChips);
                if (numberOfPlayersInPot > 1) {
                    // TODO: this is the only place at the moment, but having a
                    // player decide whether they want to show their cards is
                    // another use case
                    playersToShowCardsMap.add(winner);
                }
            }
        }
    }

    private void addToChipsInBack(UUID winnerOfBonusChips, int chipsToAdd) {
        int currentAmount = chipsInBackMap.get(winnerOfBonusChips);
        chipsInBackMap.put(winnerOfBonusChips, currentAmount + chipsToAdd);
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

}
