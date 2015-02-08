package com.flexpoker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Pot {

    private final UUID entityId;

    private final List<HandEvaluation> handEvaluationList;

    private final Set<UUID> playersInvolved;

    private final Map<UUID, Integer> chipsForPlayerToWin;

    private int amount;

    private boolean open;

    public Pot(UUID entityId, List<HandEvaluation> handEvaluationList) {
        this.entityId = entityId;
        this.handEvaluationList = handEvaluationList;
        playersInvolved = handEvaluationList.stream().map(x -> x.getPlayerId())
                .collect(Collectors.toSet());
        chipsForPlayerToWin = new HashMap<>();
        open = true;
        recalculateWinners();
    }

    public int getChipsWon(UUID playerInHand) {
        return chipsForPlayerToWin.getOrDefault(playerInHand, Integer.valueOf(0))
                .intValue();
    }

    public boolean forcePlayerToShowCards(UUID playerInHand) {
        return getChipsWon(playerInHand) > 0 && playersInvolved.size() > 1;
    }

    public void addChips(int chips) {
        amount += chips;
        recalculateWinners();
    }

    public void removePlayer(UUID playerId) {
        playersInvolved.remove(playerId);
        recalculateWinners();
    }

    public boolean isOpen() {
        return open;
    }

    public void closePot() {
        open = false;
    }

    private void recalculateWinners() {
        List<HandEvaluation> sortedHandEvaluations = new ArrayList<>(handEvaluationList);
        Collections.sort(sortedHandEvaluations);
        Collections.reverse(sortedHandEvaluations);

        List<HandEvaluation> relevantHandEvaluationsForPot = sortedHandEvaluations
                .stream().filter(x -> playersInvolved.contains(x.getPlayerId()))
                .collect(Collectors.toList());

        HandEvaluation topAssignedHand = null;
        Set<UUID> winners = new HashSet<>();

        for (HandEvaluation handEvaluation : relevantHandEvaluationsForPot) {
            if (topAssignedHand == null || topAssignedHand.compareTo(handEvaluation) == 0) {
                topAssignedHand = handEvaluation;
                winners.add(topAssignedHand.getPlayerId());
            }
        }

        int numberOfWinners = winners.size();
        int baseNumberOfChips = amount / numberOfWinners;
        int bonusChips = amount % numberOfWinners;

        chipsForPlayerToWin.clear();

        winners.forEach(x -> {
            chipsForPlayerToWin.put(x, Integer.valueOf(baseNumberOfChips));
        });

        if (bonusChips > 0) {
            // TODO: randomize this (or maybe being in a Set is good enough)
            UUID winnerOfBonusChips = winners.stream().findFirst().get();
            chipsForPlayerToWin.put(
                    winnerOfBonusChips,
                    Integer.valueOf(chipsForPlayerToWin.get(winnerOfBonusChips)
                            .intValue() + bonusChips));
        }

    }

}
