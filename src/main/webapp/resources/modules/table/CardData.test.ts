import { describe, expect, it } from 'vitest'
import CardData from './CardData'

describe('CardData', () => {
  it('contains one url for each card in a standard deck', () => {
    expect(CardData).toHaveLength(52)
  })

  it('builds card image urls from suit and rank', () => {
    expect(CardData[0]).toBe('/resources/img/nicubunu_Ornamental_deck_2_of_hearts.svg')
    expect(CardData[13]).toBe('/resources/img/nicubunu_Ornamental_deck_2_of_spades.svg')
    expect(CardData[51]).toBe('/resources/img/nicubunu_Ornamental_deck_Ace_of_clubs.svg')
  })
})