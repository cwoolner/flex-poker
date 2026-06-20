import { describe, expect, it } from 'vitest'
import { screen } from '@testing-library/react'
import { Route, Routes } from 'react-router-dom'
import InterceptRedirect from './InterceptRedirect'
import { renderWithProviders } from '../../test-utils/renderWithProviders'

describe('InterceptRedirect', () => {
  it('renders children when there is no redirect', () => {
    renderWithProviders(
      <InterceptRedirect>
        <div>lobby content</div>
      </InterceptRedirect>,
      { preloadedState: { redirectUrl: null } },
    )

    expect(screen.getByText('lobby content')).toBeInTheDocument()
  })

  it('navigates to the redirect url when one is set', () => {
    renderWithProviders(
      <Routes>
        <Route
          path="/"
          element={
            <InterceptRedirect>
              <div>lobby content</div>
            </InterceptRedirect>
          }
        />
        <Route path="/game/:gameId" element={<div>game page</div>} />
      </Routes>,
      {
        preloadedState: { redirectUrl: '/game/game-1' },
        route: '/',
      },
    )

    expect(screen.getByText('game page')).toBeInTheDocument()
    expect(screen.queryByText('lobby content')).not.toBeInTheDocument()
  })

  it('clears the redirect from state after handling it', () => {
    const { store } = renderWithProviders(
      <InterceptRedirect>
        <div>lobby content</div>
      </InterceptRedirect>,
      { preloadedState: { redirectUrl: '/game/game-1' } },
    )

    expect(store.getState().redirectUrl).toBe(null)
  })
})