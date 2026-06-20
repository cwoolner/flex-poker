import { describe, expect, it } from 'vitest'
import { render, screen } from '@testing-library/react'
import ChatLine from './ChatLine'

describe('ChatLine', () => {
  it('renders a user chat message', () => {
    render(<ChatLine chat={{ systemMessage: false, senderUsername: 'player1', message: 'hello table' }} />)

    expect(screen.getByText('player1: hello table')).toBeInTheDocument()
  })

  it('renders a system chat message', () => {
    render(<ChatLine chat={{ systemMessage: true, senderUsername: null, message: 'player2 joined' }} />)

    expect(screen.getByText('System: player2 joined')).toBeInTheDocument()
  })
})