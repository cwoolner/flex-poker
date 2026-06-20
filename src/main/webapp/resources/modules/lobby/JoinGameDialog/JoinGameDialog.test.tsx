import { describe, expect, it, vi } from 'vitest'
import { fireEvent, render, screen, within } from '@testing-library/react'
import JoinGameDialog from './JoinGameDialog'

describe('JoinGameDialog', () => {
  it('does not render when the modal is hidden', () => {
    render(
      <JoinGameDialog
        gameId="game-1"
        showModal={false}
        hideDialogCallback={vi.fn()}
        submitFormCallback={vi.fn()}
      />,
    )

    expect(screen.queryByRole('dialog')).not.toBeInTheDocument()
  })

  it('renders join details when the modal is visible', () => {
    render(
      <JoinGameDialog
        gameId="game-1"
        showModal={true}
        hideDialogCallback={vi.fn()}
        submitFormCallback={vi.fn()}
      />,
    )

    const dialog = screen.getByRole('dialog')

    expect(within(dialog).getByText('Join Game', { selector: '.modal-title' })).toBeInTheDocument()
    expect(within(dialog).getByLabelText('Current Balance')).toHaveValue('100')
    expect(within(dialog).getByLabelText('Cost')).toHaveValue('10')
    expect(within(dialog).getByLabelText('Remaining Balance')).toHaveValue('90')
  })

  it('calls hideDialogCallback when Close is clicked', () => {
    const hideDialogCallback = vi.fn()

    render(
      <JoinGameDialog
        gameId="game-1"
        showModal={true}
        hideDialogCallback={hideDialogCallback}
        submitFormCallback={vi.fn()}
      />,
    )

    const footerCloseButton = within(screen.getByRole('dialog'))
      .getAllByRole('button', { name: 'Close' })
      .find(button => button.classList.contains('btn-secondary'))

    fireEvent.click(footerCloseButton!)

    expect(hideDialogCallback).toHaveBeenCalledOnce()
  })

  it('calls submitFormCallback when the form is submitted', () => {
    const submitFormCallback = vi.fn(event => event.preventDefault())

    render(
      <JoinGameDialog
        gameId="game-1"
        showModal={true}
        hideDialogCallback={vi.fn()}
        submitFormCallback={submitFormCallback}
      />,
    )

    fireEvent.submit(document.getElementById('join-game-form')!)

    expect(submitFormCallback).toHaveBeenCalledOnce()
  })
})