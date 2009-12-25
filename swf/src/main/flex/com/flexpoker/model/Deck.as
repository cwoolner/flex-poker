package com.flexpoker.model {

    import flash.errors.IllegalOperationError;

    public class Deck {

        private static var instance:Deck = new Deck();

        [Embed(source="/assets/nicubunu_Card_backs_cards_blue.svg")]
        public var blankCard:Class;

        private var array:Array = new Array();

        [Embed(source="/assets/nicubunu_Ornamental_deck_2_of_hearts.svg")]
        private var card0:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_3_of_hearts.svg")]
        private var card1:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_4_of_hearts.svg")]
        private var card2:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_5_of_hearts.svg")]
        private var card3:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_6_of_hearts.svg")]
        private var card4:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_7_of_hearts.svg")]
        private var card5:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_8_of_hearts.svg")]
        private var card6:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_9_of_hearts.svg")]
        private var card7:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_10_of_hearts.svg")]
        private var card8:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_Jack_of_hearts.svg")]
        private var card9:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_Queen_of_hearts.svg")]
        private var card10:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_King_of_hearts.svg")]
        private var card11:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_Ace_of_hearts.svg")]
        private var card12:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_2_of_spades.svg")]
        private var card13:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_3_of_spades.svg")]
        private var card14:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_4_of_spades.svg")]
        private var card15:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_5_of_spades.svg")]
        private var card16:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_6_of_spades.svg")]
        private var card17:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_7_of_spades.svg")]
        private var card18:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_8_of_spades.svg")]
        private var card19:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_9_of_spades.svg")]
        private var card20:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_10_of_spades.svg")]
        private var card21:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_Jack_of_spades.svg")]
        private var card22:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_Queen_of_spades.svg")]
        private var card23:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_King_of_spades.svg")]
        private var card24:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_Ace_of_spades.svg")]
        private var card25:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_2_of_diamonds.svg")]
        private var card26:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_3_of_diamonds.svg")]
        private var card27:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_4_of_diamonds.svg")]
        private var card28:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_5_of_diamonds.svg")]
        private var card29:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_6_of_diamonds.svg")]
        private var card30:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_7_of_diamonds.svg")]
        private var card31:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_8_of_diamonds.svg")]
        private var card32:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_9_of_diamonds.svg")]
        private var card33:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_10_of_diamonds.svg")]
        private var card34:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_Jack_of_diamonds.svg")]
        private var card35:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_Queen_of_diamonds.svg")]
        private var card36:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_King_of_diamonds.svg")]
        private var card37:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_Ace_of_diamonds.svg")]
        private var card38:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_2_of_clubs.svg")]
        private var card39:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_3_of_clubs.svg")]
        private var card40:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_4_of_clubs.svg")]
        private var card41:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_5_of_clubs.svg")]
        private var card42:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_6_of_clubs.svg")]
        private var card43:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_7_of_clubs.svg")]
        private var card44:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_8_of_clubs.svg")]
        private var card45:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_9_of_clubs.svg")]
        private var card46:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_10_of_clubs.svg")]
        private var card47:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_Jack_of_clubs.svg")]
        private var card48:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_Queen_of_clubs.svg")]
        private var card49:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_King_of_clubs.svg")]
        private var card50:Class;

        [Embed(source="/assets/nicubunu_Ornamental_deck_Ace_of_clubs.svg")]
        private var card51:Class;

        public function Deck() {
            if (instance != null) {
                throw new IllegalOperationError("Deck is a Singleton.");
            }

            array[0] = card0;
            array[1] = card1;
            array[2] = card2;
            array[3] = card3;
            array[4] = card4;
            array[5] = card5;
            array[6] = card6;
            array[7] = card7;
            array[8] = card8;
            array[9] = card9;
            array[10] = card10;
            array[11] = card11;
            array[12] = card12;
            array[13] = card13;
            array[14] = card14;
            array[15] = card15;
            array[16] = card16;
            array[17] = card17;
            array[18] = card18;
            array[19] = card19;
            array[20] = card20;
            array[21] = card21;
            array[22] = card22;
            array[23] = card23;
            array[24] = card24;
            array[25] = card25;
            array[26] = card26;
            array[27] = card27;
            array[28] = card28;
            array[29] = card29;
            array[30] = card30;
            array[31] = card31;
            array[32] = card32;
            array[33] = card33;
            array[34] = card34;
            array[35] = card35;
            array[36] = card36;
            array[37] = card37;
            array[38] = card38;
            array[39] = card39;
            array[40] = card40;
            array[41] = card41;
            array[42] = card42;
            array[43] = card43;
            array[44] = card44;
            array[45] = card45;
            array[46] = card46;
            array[47] = card47;
            array[48] = card48;
            array[49] = card49;
            array[50] = card50;
            array[51] = card51;
        }

        public static function getInstance():Deck {
            return instance;
        }

        public function getCardImage(card:Card):Class {
            return array[card.id];
        }

    }

}
