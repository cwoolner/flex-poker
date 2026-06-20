import { describe, expect, it, vi } from 'vitest'
import { fireEvent, render, screen } from '@testing-library/react'
import GameList from './GameList'

const sampleGame = {
  id: 'game-1',
  name: 'Friday Night',
  stage: 'REGISTERING',
  numberOfRegisteredPlayers: 3,
  maxNumberOfPlayers: 9,
  maxPlayersPerTable: 6,
  createdBy: 'player1',
  createdOn: '2026-06-20T18:30:00-05:00',
}

describe('GameList', () => {
  it('shows an empty state when there are no games', () => {
    render(
      <GameList
        gameList={[]}
        gameOpenedCallback={vi.fn()}
        openCreateGameModalCallback={vi.fn()}
      />,
    )

    expect(screen.getByText('No games to show')).toBeInTheDocument()
  })

  it('renders game details in the table', () => {
    render(
      <GameList
        gameList={[sampleGame]}
        gameOpenedCallback={vi.fn()}
        openCreateGameModalCallback={vi.fn()}
      />,
    )

    expect(screen.getByText('Friday Night')).toBeInTheDocument()
    expect(screen.getByText('REGISTERING')).toBeInTheDocument()
    expect(screen.getByText('3')).toBeInTheDocument()
    expect(screen.getByText('9')).toBeInTheDocument()
    expect(screen.getByText('6')).toBeInTheDocument()
    expect(screen.getByText('player1')).toBeInTheDocument()
  })

  it('enables Open only for games in REGISTERING stage', () => {
    const registeringGame = { ...sampleGame, id: 'game-1', stage: 'REGISTERING' }
    const inProgressGame = {
      ...sampleGame,
      id: 'game-2',
      name: 'Saturday Game',
      stage: 'INPROGRESS',
    }

    render(
      <GameList
        gameList={[registeringGame, inProgressGame]}
        gameOpenedCallback={vi.fn()}
        openCreateGameModalCallback={vi.fn()}
      />,
    )

    const openButtons = screen.getAllByRole('button', { name: 'Open' })

    expect(openButtons[0]).toBeEnabled()
    expect(openButtons[1]).toBeDisabled()
  })

  it('calls gameOpenedCallback with the game id when Open is clicked', () => {
    const gameOpenedCallback = vi.fn()

    render(
      <GameList
        gameList={[sampleGame]}
        gameOpenedCallback={gameOpenedCallback}
        openCreateGameModalCallback={vi.fn()}
      />,
    )

    fireEvent.click(screen.getByRole('button', { name: 'Open' }))

    expect(gameOpenedCallback).toHaveBeenCalledWith('game-1')
  })

  it('calls openCreateGameModalCallback when the add button is clicked', () => {
    const openCreateGameModalCallback = vi.fn()

    render(
      <GameList
        gameList={[]}
        gameOpenedCallback={vi.fn()}
        openCreateGameModalCallback={openCreateGameModalCallback}
      />,
    )

    fireEvent.click(screen.getByRole('button', { name: '+' }))

    expect(openCreateGameModalCallback).toHaveBeenCalledOnce()
  })
})