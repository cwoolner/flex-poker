package com.flexpoker.test.util.datageneration

import com.flexpoker.table.command.CardsUsedInHand

object DeckGenerator {

    fun createDeck(): CardsUsedInHand {
        return CardsUsedInHand(
            CardGenerator.createFlopCards(),
            CardGenerator.createTurnCard(),
            CardGenerator.createRiverCard(),
            listOf(CardGenerator.createPocketCards1(), CardGenerator.createPocketCards2())
        )
    }

}