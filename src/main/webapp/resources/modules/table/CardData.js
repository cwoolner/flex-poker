import _ from 'lodash'

const urlTemplate = (cardSuit, cardRank) => `/resources/img/nicubunu_Ornamental_deck_${cardRank}_of_${cardSuit}.svg`
const cardSuits = ['hearts', 'spades', 'diamonds', 'clubs']
const cardRanks = ['2', '3', '4', '5', '6', '7', '8', '9', '10', 'Jack', 'Queen', 'King', 'Ace']

export default _.flatten(
  cardSuits.map(cardSuit => cardRanks.map(cardRank => urlTemplate(cardSuit, cardRank)))
)
