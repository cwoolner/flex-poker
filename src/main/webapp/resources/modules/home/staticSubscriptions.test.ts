import { beforeEach, describe, expect, it, vi } from 'vitest'
import {
  initOpenGameTabs,
  pocketCardsReceived,
  redirectToTable,
  updateOpenGameList,
  updateOpenGameTabs,
} from '../../reducers'

const { subscribe } = vi.hoisted(() => ({
  subscribe: vi.fn(),
}))

vi.mock('../webSocket/WebSocketSubscriptionManager', () => ({
  default: { subscribe },
}))

import staticSubscriptions from './staticSubscriptions'

const subscriptionByLocation = () =>
  Object.fromEntries(
    subscribe.mock.calls[0][1].map(({ location, subscription }) => [location, subscription]),
  )

describe('staticSubscriptions', () => {
  beforeEach(() => {
    subscribe.mockClear()
    vi.stubGlobal('alert', vi.fn())
  })

  it('registers all static websocket subscriptions on startup', () => {
    staticSubscriptions(vi.fn())

    expect(subscribe).toHaveBeenCalledOnce()
    expect(subscribe.mock.calls[0][1].map(x => x.location)).toEqual([
      '/user/queue/pocketcards',
      '/user/queue/errors',
      '/app/opengamesforuser',
      '/user/queue/opengamesforuser',
      '/user/queue/opentable',
      '/user/queue/personaltablestatus',
      '/topic/availabletournaments',
    ])
  })

  it('dispatches pocket cards for a single websocket payload', () => {
    const dispatch = vi.fn()
    staticSubscriptions(dispatch)

    const handler = subscriptionByLocation()['/user/queue/pocketcards']
    handler({ body: JSON.stringify({ handId: 'hand-1', cardId1: 10, cardId2: 22 }) })

    expect(dispatch).toHaveBeenCalledWith(
      pocketCardsReceived('hand-1', { cardId1: 10, cardId2: 22 }),
    )
  })

  it('dispatches pocket cards for each hand in an array payload', () => {
    const dispatch = vi.fn()
    staticSubscriptions(dispatch)

    const handler = subscriptionByLocation()['/user/queue/pocketcards']
    handler({
      body: JSON.stringify([
        { handId: 'hand-1', cardId1: 10, cardId2: 22 },
        { handId: 'hand-2', cardId1: 5, cardId2: 18 },
      ]),
    })

    expect(dispatch).toHaveBeenCalledWith(
      pocketCardsReceived('hand-1', { cardId1: 10, cardId2: 22 }),
    )
    expect(dispatch).toHaveBeenCalledWith(
      pocketCardsReceived('hand-2', { cardId1: 5, cardId2: 18 }),
    )
  })

  it('dispatches open game tabs from websocket payloads', () => {
    const dispatch = vi.fn()
    const openGameTabs = [{ gameId: 'game-1', name: 'Friday Night' }]
    staticSubscriptions(dispatch)

    subscriptionByLocation()['/app/opengamesforuser']({
      body: JSON.stringify(openGameTabs),
    })

    expect(dispatch).toHaveBeenCalledWith(initOpenGameTabs(openGameTabs))
  })

  it('dispatches updated open game tabs from websocket payloads', () => {
    const dispatch = vi.fn()
    const openGameTabs = [{ gameId: 'game-1' }, { gameId: 'game-2' }]
    staticSubscriptions(dispatch)

    subscriptionByLocation()['/user/queue/opengamesforuser']({
      body: JSON.stringify(openGameTabs),
    })

    expect(dispatch).toHaveBeenCalledWith(updateOpenGameTabs(openGameTabs))
  })

  it('dispatches a redirect when an open table message is received', () => {
    const dispatch = vi.fn()
    staticSubscriptions(dispatch)

    subscriptionByLocation()['/user/queue/opentable']({
      body: JSON.stringify({ gameId: 'game-1', tableId: 'table-1' }),
    })

    expect(dispatch).toHaveBeenCalledWith(redirectToTable('game-1', 'table-1'))
  })

  it('dispatches game list updates from websocket payloads', () => {
    const dispatch = vi.fn()
    const openGameList = [{ id: 'game-1', name: 'Friday Night' }]
    staticSubscriptions(dispatch)

    subscriptionByLocation()['/topic/availabletournaments']({
      body: JSON.stringify(openGameList),
    })

    expect(dispatch).toHaveBeenCalledWith(updateOpenGameList(openGameList))
  })

  it('alerts when a user error message is received', () => {
    staticSubscriptions(vi.fn())

    subscriptionByLocation()['/user/queue/errors']({ body: 'something went wrong' })

    expect(alert).toHaveBeenCalledWith('Error something went wrong')
  })

  it('alerts with personal table status payloads', () => {
    staticSubscriptions(vi.fn())

    subscriptionByLocation()['/user/queue/personaltablestatus']({
      body: JSON.stringify({ tableId: 'table-1', status: 'WAITING' }),
    })

    expect(alert).toHaveBeenCalledWith({ tableId: 'table-1', status: 'WAITING' })
  })
})