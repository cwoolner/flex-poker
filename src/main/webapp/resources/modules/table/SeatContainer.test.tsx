import { describe, expect, it } from 'vitest'
import { Map } from 'immutable'
import { screen } from '@testing-library/react'
import SeatContainer from './SeatContainer'
import { renderWithProviders } from '../../test-utils/renderWithProviders'

const seats = [
  {
    position: 1,
    name: 'player1',
    chipsInBack: 1000,
    chipsInFront: 25,
    isStillInHand: true,
    raiseTo: 0,
    callAmount: 0,
    isButton: true,
    isSmallBlind: false,
    isBigBlind: false,
    isActionOn: true,
  },
  {
    position: 2,
    name: 'player2',
    chipsInBack: 900,
    chipsInFront: 50,
    isStillInHand: true,
    raiseTo: 0,
    callAmount: 0,
    isButton: false,
    isSmallBlind: true,
    isBigBlind: false,
    isActionOn: false,
  },
]

describe('SeatContainer', () => {
  it('renders a seat for each player at the table', () => {
    const { container } = renderWithProviders(
      <SeatContainer seats={seats} mySeat={seats[0]} />,
      {
        preloadedState: {
          activeTable: { gameId: 'game-1', tableId: 'table-1' },
          actionOnTicks: Map({ 'game-1': Map({ 'table-1': 14 }) }),
        },
      },
    )

    expect(screen.getByText('player1')).toBeInTheDocument()
    expect(screen.getByText('player2')).toBeInTheDocument()
    expect(container.querySelectorAll('.my-seat')).toHaveLength(1)
    expect(screen.getByText('14')).toBeInTheDocument()
  })
})