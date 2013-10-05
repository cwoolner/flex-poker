package com.flexpoker.bso.api;

import java.util.List;

import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.HandRanking;
import com.flexpoker.model.User;
import com.flexpoker.model.card.FlopCards;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.model.card.RiverCard;
import com.flexpoker.model.card.TurnCard;

public interface HandEvaluatorBso {

    List<HandRanking> determinePossibleHands(FlopCards flopCards,
            TurnCard turnCard, RiverCard riverCard);

    HandEvaluation determineHandEvaluation(FlopCards flopCards,
            TurnCard turnCard, RiverCard riverCard, User user,
            PocketCards pocketCards, List<HandRanking> possibleHandRankings);

}
