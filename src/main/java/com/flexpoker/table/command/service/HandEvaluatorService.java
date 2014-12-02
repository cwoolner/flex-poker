package com.flexpoker.table.command.service;

import java.util.List;
import java.util.Map;

import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.HandRanking;
import com.flexpoker.model.card.FlopCards;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.model.card.RiverCard;
import com.flexpoker.model.card.TurnCard;

public interface HandEvaluatorService {

    List<HandRanking> determinePossibleHands(FlopCards flopCards, TurnCard turnCard,
            RiverCard riverCard);

    Map<PocketCards, HandEvaluation> determineHandEvaluation(FlopCards flopCards,
            TurnCard turnCard, RiverCard riverCard, List<PocketCards> pocketCards,
            List<HandRanking> possibleHandRankings);

}
