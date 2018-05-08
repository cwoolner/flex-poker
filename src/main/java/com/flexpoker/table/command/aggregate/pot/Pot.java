package com.flexpoker.table.command.aggregate.pot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.table.command.aggregate.HandEvaluation;

public class Pot {

    private final UUID entityId;

    private final Set<HandEvaluation> handEvaluations;

    private final Set<UUID> playersInvolved;

    private final Map<UUID, Integer> chipsForPlayerToWin;

    private int amount;

    private boolean open;

    public Pot(UUID entityId, Set<HandEvaluation> handEvaluations) {
        this.entityId = entityId;
        this.handEvaluations = handEvaluations;
        this.playersInvolved = handEvaluations.stream().map(x -> x.getPlayerId())
                .collect(Collectors.toSet());
        chipsForPlayerToWin = new HashMap<>();
        open = true;
    }

    public UUID getId() {
        return entityId;
    }

    public int getChipsWon(UUID playerInHand) {
        return chipsForPlayerToWin.getOrDefault(playerInHand, Integer.valueOf(0))
                .intValue();
    }

    public boolean forcePlayerToShowCards(UUID playerInHand) {
        return getChipsWon(playerInHand) > 0 && playersInvolved.size() > 1;
    }

    public void addChips(int chips) {
        if (!open) {
            throw new FlexPokerException("cannot add chips to a closed pot");
        }
        amount += chips;
        recalculateWinners();
    }

    public void removePlayer(UUID playerId) {
        if (!open) {
            throw new FlexPokerException("cannot remove player from a closed pot");
        }
        playersInvolved.remove(playerId);
        handEvaluations.removeIf(x -> x.getPlayerId().equals(playerId));
        recalculateWinners();
    }

    public boolean isOpen() {
        return open;
    }

    public void closePot() {
        open = false;
    }

    private void recalculateWinners() {
        var relevantHandEvaluationsForPot = handEvaluations.stream()
                .filter(x -> playersInvolved.contains(x.getPlayerId()))
                .sorted((x, y) -> y.compareTo(x))
                .collect(Collectors.toList());

        HandEvaluation topAssignedHand = null;
        var winners = new ArrayList<UUID>();

        for (var handEvaluation : relevantHandEvaluationsForPot) {
            if (topAssignedHand == null || topAssignedHand.compareTo(handEvaluation) == 0) {
                topAssignedHand = handEvaluation;
                winners.add(topAssignedHand.getPlayerId());
            }
        }

        var numberOfWinners = winners.size();
        var baseNumberOfChips = amount / numberOfWinners;
        var bonusChips = amount % numberOfWinners;

        chipsForPlayerToWin.clear();

        winners.forEach(x -> chipsForPlayerToWin.put(x, baseNumberOfChips));

        if (bonusChips >= 1) {
            var randomNumber = new Random(System.currentTimeMillis()).nextInt(winners.size());
            chipsForPlayerToWin.compute(winners.get(randomNumber), (playerId, chips) -> chips + bonusChips);
        }

    }

}
