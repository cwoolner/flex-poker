package com.flexpoker.table.command.service

import com.flexpoker.table.command.FlopCards
import com.flexpoker.table.command.HandRanking
import com.flexpoker.table.command.PocketCards
import com.flexpoker.table.command.RiverCard
import com.flexpoker.table.command.TurnCard
import com.flexpoker.table.command.aggregate.HandEvaluation

interface HandEvaluatorService {
    fun determinePossibleHands(flopCards: FlopCards, turnCard: TurnCard, riverCard: RiverCard): List<HandRanking>
    fun determineHandEvaluation(flopCards: FlopCards, turnCard: TurnCard, riverCard: RiverCard,
                                pocketCards: List<PocketCards>, possibleHandRankings: List<HandRanking>): Map<PocketCards, HandEvaluation>
}