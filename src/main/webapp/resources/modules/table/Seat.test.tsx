import { describe, expect, it } from 'vitest'
import { render, screen } from '@testing-library/react'
import Seat from './Seat'

const baseSeat = {
  position: 2,
  name: 'player1',
  chipsInBack: 1000,
  chipsInFront: 50,
  isStillInHand: true,
  raiseTo: 0,
  callAmount: 0,
  isButton: false,
  isSmallBlind: false,
  isBigBlind: false,
  isActionOn: false,
}

describe('Seat', () => {
  it('renders seat details and data attributes', () => {
    const { container } = render(
      <Seat seat={baseSeat} mySeat={false} actionOnTick={null} />,
    )

    expect(screen.getByText('player1')).toBeInTheDocument()
    expect(screen.getByText('50')).toBeInTheDocument()
    expect(screen.getByText('1000')).toBeInTheDocument()

    const seatElement = container.firstChild as HTMLElement
    expect(seatElement).toHaveAttribute('data-position', '2')
    expect(seatElement).toHaveAttribute('data-action-on', 'false')
    expect(seatElement).toHaveAttribute('data-still-in-hand', 'true')
    expect(seatElement).not.toHaveClass('my-seat')
  })

  it('marks the current players seat and shows the action-on timer', () => {
    render(
      <Seat
        seat={{ ...baseSeat, isActionOn: true }}
        mySeat={true}
        actionOnTick={9}
      />,
    )

    expect(screen.getByText('9')).toBeInTheDocument()
    expect(screen.getByText('player1').parentElement).toHaveClass('my-seat')
  })

  it('shows blind and button markers when applicable', () => {
    const { container } = render(
      <Seat
        seat={{
          ...baseSeat,
          isButton: true,
          isSmallBlind: true,
          isBigBlind: true,
        }}
        mySeat={false}
        actionOnTick={null}
      />,
    )

    const images = container.querySelectorAll('img')
    expect(images).toHaveLength(3)
    expect(images[0]).toHaveAttribute('src', '/resources/img/button.png')
    expect(images[1]).toHaveAttribute('src', '/resources/img/smallBlind.png')
    expect(images[2]).toHaveAttribute('src', '/resources/img/bigBlind.png')
  })
})