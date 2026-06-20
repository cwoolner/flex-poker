import { describe, expect, it, vi } from 'vitest'
import { fireEvent, render, screen, within } from '@testing-library/react'
import CreateGameDialog from './CreateGameDialog'

describe('CreateGameDialog', () => {
  it('does not render when the modal is hidden', () => {
    render(
      <CreateGameDialog
        showModal={false}
        hideDialogCallback={vi.fn()}
        submitFormCallback={vi.fn()}
      />,
    )

    expect(screen.queryByRole('dialog')).not.toBeInTheDocument()
  })

  it('renders create game fields when the modal is visible', () => {
    render(
      <CreateGameDialog
        showModal={true}
        hideDialogCallback={vi.fn()}
        submitFormCallback={vi.fn()}
      />,
    )

    const dialog = screen.getByRole('dialog')

    expect(within(dialog).getByText('Create Game', { selector: '.modal-title' })).toBeInTheDocument()
    expect(within(dialog).getByLabelText('Name')).toBeInTheDocument()
    expect(within(dialog).getByLabelText('Number of Players (2 - 90)')).toBeInTheDocument()
    expect(within(dialog).getByLabelText('Number of Players per Table (2 - 9)')).toBeInTheDocument()
    expect(within(dialog).getByLabelText('Blind increment in minutes (1 - 60)')).toBeInTheDocument()
    expect(within(dialog).getByLabelText('Blind timer in seconds (1 - 60)')).toBeInTheDocument()
  })

  it('calls hideDialogCallback when Close is clicked', () => {
    const hideDialogCallback = vi.fn()

    render(
      <CreateGameDialog
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
      <CreateGameDialog
        showModal={true}
        hideDialogCallback={vi.fn()}
        submitFormCallback={submitFormCallback}
      />,
    )

    fireEvent.submit(document.getElementById('create-game-form')!)

    expect(submitFormCallback).toHaveBeenCalledOnce()
  })
})