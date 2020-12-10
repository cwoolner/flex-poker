package com.flexpoker.table.command.service;

import java.util.List;
import java.util.Map;

import com.flexpoker.table.command.HandRanking;
import com.flexpoker.table.command.FlopCards;
import com.flexpoker.table.command.PocketCards;
import com.flexpoker.table.command.RiverCard;
import com.flexpoker.table.command.TurnCard;
import com.flexpoker.table.command.aggregate.HandEvaluation;

public interface HandEvaluatorService {

    List<HandRanking> determinePossibleHands(FlopCards flopCards, TurnCard turnCard,
            RiverCard riverCard);

    Map<PocketCards, HandEvaluation> determineHandEvaluation(FlopCards flopCards,
            TurnCard turnCard, RiverCard riverCard, List<PocketCards> pocketCards,
            List<HandRanking> possibleHandRankings);

}
