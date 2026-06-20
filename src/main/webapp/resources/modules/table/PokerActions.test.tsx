import { beforeEach, describe, expect, it, vi } from 'vitest'
import { fireEvent, render, screen } from '@testing-library/react'
import PokerActions from './PokerActions'

const { send } = vi.hoisted(() => ({
  send: vi.fn(),
}))

vi.mock('../webSocket/WebSocketService', () => ({
  default: { send },
}))

const defaultProps = {
  gameId: 'game-1',
  tableId: 'table-1',
  actionOn: true,
  callAmount: 0,
  minRaiseTo: 20,
  maxRaiseTo: 100,
}

const visibleActionButtons = (container: HTMLElement) =>
  [...container.querySelectorAll('div:not(.hidden) > button')]
    .filter(button => !button.classList.contains('hidden'))

describe('PokerActions', () => {
  beforeEach(() => {
    send.mockClear()
  })

  it('shows disabled checkbox actions when it is not the players turn', () => {
    render(<PokerActions {...defaultProps} actionOn={false} />)

    expect(screen.getByLabelText('Check')).toHaveAttribute('type', 'checkbox')
    expect(screen.getByLabelText('Call')).toHaveAttribute('type', 'checkbox')
    expect(screen.getByLabelText('Raise')).toHaveAttribute('type', 'checkbox')
    expect(screen.getByLabelText('Fold')).toHaveAttribute('type', 'checkbox')
  })

  it('shows Check when no call amount is required', () => {
    const { container } = render(<PokerActions {...defaultProps} callAmount={0} />)

    expect(visibleActionButtons(container).map(button => button.textContent)).toEqual([
      'Check',
      'Raise to 20',
    ])
  })

  it('shows Call and Fold when a call amount is required', () => {
    const { container } = render(<PokerActions {...defaultProps} callAmount={15} />)

    expect(visibleActionButtons(container).map(button => button.textContent)).toEqual([
      'Call 15',
      'Raise to 20',
      'Fold',
    ])
  })

  it('hides Raise when raising is not allowed', () => {
    const { container } = render(<PokerActions {...defaultProps} minRaiseTo={0} maxRaiseTo={0} />)

    expect(visibleActionButtons(container).map(button => button.textContent)).toEqual(['Check'])
  })

  it('sends a check action over the websocket', () => {
    const { container } = render(<PokerActions {...defaultProps} callAmount={0} />)

    fireEvent.click(visibleActionButtons(container)[0])

    expect(send).toHaveBeenCalledWith('/app/check', { gameId: 'game-1', tableId: 'table-1' })
  })

  it('sends a call action over the websocket', () => {
    const { container } = render(<PokerActions {...defaultProps} callAmount={15} />)

    fireEvent.click(visibleActionButtons(container)[0])

    expect(send).toHaveBeenCalledWith('/app/call', { gameId: 'game-1', tableId: 'table-1' })
  })

  it('sends a fold action over the websocket', () => {
    const { container } = render(<PokerActions {...defaultProps} callAmount={15} />)

    fireEvent.click(visibleActionButtons(container)[2])

    expect(send).toHaveBeenCalledWith('/app/fold', { gameId: 'game-1', tableId: 'table-1' })
  })

  it('sends a raise action when the raise amount is valid', () => {
    const { container } = render(<PokerActions {...defaultProps} callAmount={15} />)

    fireEvent.click(visibleActionButtons(container)[1])

    expect(send).toHaveBeenCalledWith('/app/raise', {
      gameId: 'game-1',
      tableId: 'table-1',
      raiseToAmount: 20,
    })
  })

  it('hides the invalid raise warning when the raise amount is valid', () => {
    render(<PokerActions {...defaultProps} callAmount={15} />)

    expect(screen.getByText('Invalid raise')).toHaveClass('hidden')
    expect(screen.getByRole('button', { name: 'Raise to 20' })).toBeInTheDocument()
  })
})