import { describe, expect, it } from 'vitest'
import { fireEvent, render, screen } from '@testing-library/react'
import { MemoryRouter } from 'react-router-dom'
import GameTab from './GameTab'

const baseOpenGameTab = {
  gameId: 'game-1',
  name: 'Friday Night',
  myTableId: null,
  viewingTables: [],
}

const renderGameTab = (openGameTab: typeof baseOpenGameTab) =>
  render(
    <MemoryRouter>
      <GameTab openGameTab={openGameTab} />
    </MemoryRouter>,
  )

const openDropdown = () => {
  fireEvent.click(screen.getByRole('button', { name: 'Friday Night' }))
}

describe('GameTab', () => {
  it('renders the game name as the dropdown title', () => {
    renderGameTab(baseOpenGameTab)

    expect(screen.getByRole('button', { name: 'Friday Night' })).toBeInTheDocument()
  })

  it('shows a My Table link when the player has a table', () => {
    renderGameTab({ ...baseOpenGameTab, myTableId: 'table-1' })
    openDropdown()

    const myTableLink = screen.getByRole('link', { name: 'My Table' })

    expect(myTableLink).toHaveAttribute('href', '/game/game-1/table/table-1')
  })

  it('hides the My Table link when the player has no table', () => {
    renderGameTab(baseOpenGameTab)
    openDropdown()

    expect(screen.queryByRole('link', { name: 'My Table' })).not.toBeInTheDocument()
  })

  it('always shows a Game Page link', () => {
    renderGameTab(baseOpenGameTab)
    openDropdown()

    const gamePageLink = screen.getByRole('link', { name: 'Game Page' })

    expect(gamePageLink).toHaveAttribute('href', '/game/game-1')
  })

  it('shows links for each viewing table', () => {
    renderGameTab({
      ...baseOpenGameTab,
      viewingTables: ['table-2', 'table-3'],
    })
    openDropdown()

    const tableTwoLink = screen.getByRole('link', { name: 'table-2' })
    const tableThreeLink = screen.getByRole('link', { name: 'table-3' })

    expect(tableTwoLink).toHaveAttribute('href', '/game/game-1/table/table-2')
    expect(tableThreeLink).toHaveAttribute('href', '/game/game-1/table/table-3')
  })

  it('renders a divider only when viewing tables exist', () => {
    const { container, rerender } = render(
      <MemoryRouter>
        <GameTab openGameTab={baseOpenGameTab} />
      </MemoryRouter>,
    )

    openDropdown()
    expect(container.querySelector('.dropdown-divider')).not.toBeInTheDocument()

    rerender(
      <MemoryRouter>
        <GameTab openGameTab={{ ...baseOpenGameTab, viewingTables: ['table-2'] }} />
      </MemoryRouter>,
    )

    openDropdown()
    expect(container.querySelector('.dropdown-divider')).toBeInTheDocument()
  })
})