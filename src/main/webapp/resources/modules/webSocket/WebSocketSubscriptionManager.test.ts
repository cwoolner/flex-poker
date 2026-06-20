import { beforeEach, describe, expect, it, vi } from 'vitest'

const { registerSubscription } = vi.hoisted(() => ({
  registerSubscription: vi.fn((location: string) =>
    Promise.resolve({ unsubscribe: vi.fn(), location })),
}))

vi.mock('./WebSocketService', () => ({
  default: {
    registerSubscription,
    send: vi.fn(),
    connect: vi.fn(),
    disconnect: vi.fn(),
  },
}))

import WebSocketSubscriptionManager from './WebSocketSubscriptionManager'

describe('WebSocketSubscriptionManager', () => {
  beforeEach(() => {
    registerSubscription.mockClear()
  })

  it('registers a new websocket subscription for a component', () => {
    const component = { id: 'component-1' }
    const handler = vi.fn()

    WebSocketSubscriptionManager.subscribe(component, [
      { location: '/topic/chat/lobby', subscription: handler },
    ])

    expect(registerSubscription).toHaveBeenCalledOnce()
    expect(registerSubscription).toHaveBeenCalledWith('/topic/chat/lobby', handler)
  })

  it('does not register duplicate subscriptions for the same component and location', () => {
    const component = { id: 'component-2' }
    const handler = vi.fn()
    const subscription = { location: '/topic/chat/lobby', subscription: handler }

    WebSocketSubscriptionManager.subscribe(component, [subscription])
    WebSocketSubscriptionManager.subscribe(component, [subscription])

    expect(registerSubscription).toHaveBeenCalledOnce()
  })

  it('unsubscribes from all subscriptions for a component', async () => {
    const component = { id: 'component-3' }
    const lobbyUnsubscribe = vi.fn()
    const gameUnsubscribe = vi.fn()

    registerSubscription
      .mockResolvedValueOnce({ unsubscribe: lobbyUnsubscribe })
      .mockResolvedValueOnce({ unsubscribe: gameUnsubscribe })

    WebSocketSubscriptionManager.subscribe(component, [
      { location: '/topic/chat/lobby', subscription: vi.fn() },
      { location: '/topic/chat/game/game-1', subscription: vi.fn() },
    ])

    WebSocketSubscriptionManager.unsubscribe(component)
    await Promise.resolve()

    expect(lobbyUnsubscribe).toHaveBeenCalledOnce()
    expect(gameUnsubscribe).toHaveBeenCalledOnce()
  })
})