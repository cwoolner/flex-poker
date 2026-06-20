import { beforeEach, describe, expect, it, vi } from 'vitest'
import { INITITAL_APP_STATE } from '../../reducers/types'
import { actionOnTickReceived, tableUpdateReceived } from '../../reducers'

const { subscribe } = vi.hoisted(() => ({
  subscribe: vi.fn(),
}))

vi.mock('../webSocket/WebSocketSubscriptionManager', () => ({
  default: { subscribe },
}))

import tableStateSubscriber from './tableStateSubscriber'

const createStore = (activeTable: { gameId: string | null, tableId: string | null }) => ({
  getState: () => ({
    ...INITITAL_APP_STATE,
    activeTable,
  }),
})

describe('tableStateSubscriber', () => {
  beforeEach(() => {
    subscribe.mockClear()
  })

  it('does not subscribe when no active table is selected', () => {
    const dispatch = vi.fn()
    const onStoreChange = tableStateSubscriber(dispatch)

    onStoreChange(createStore({ gameId: null, tableId: null }))()

    expect(subscribe).not.toHaveBeenCalled()
  })

  it('does not subscribe when only a game is active', () => {
    const dispatch = vi.fn()
    const onStoreChange = tableStateSubscriber(dispatch)

    onStoreChange(createStore({ gameId: 'game-1', tableId: null }))()

    expect(subscribe).not.toHaveBeenCalled()
  })

  it('subscribes to table state and action-on tick topics when a table is active', () => {
    const dispatch = vi.fn()
    const onStoreChange = tableStateSubscriber(dispatch)

    onStoreChange(createStore({ gameId: 'game-1', tableId: 'table-1' }))()

    expect(subscribe).toHaveBeenCalledOnce()
    expect(subscribe.mock.calls[0][1]).toEqual([
      {
        location: '/topic/game/game-1/table/table-1',
        subscription: expect.any(Function),
      },
      {
        location: '/topic/game/game-1/table/table-1/action-on-tick',
        subscription: expect.any(Function),
      },
    ])
  })

  it('dispatches table updates from websocket payloads', () => {
    const dispatch = vi.fn()
    const onStoreChange = tableStateSubscriber(dispatch)
    const tableState = { id: 'table-1', version: 2, seats: [], totalPot: 100 }

    onStoreChange(createStore({ gameId: 'game-1', tableId: 'table-1' }))()

    const tableUpdateHandler = subscribe.mock.calls[0][1][0].subscription
    tableUpdateHandler({ body: JSON.stringify(tableState) })

    expect(dispatch).toHaveBeenCalledWith(
      tableUpdateReceived('game-1', 'table-1', tableState),
    )
  })

  it('dispatches action-on ticks from websocket payloads', () => {
    const dispatch = vi.fn()
    const onStoreChange = tableStateSubscriber(dispatch)

    onStoreChange(createStore({ gameId: 'game-1', tableId: 'table-1' }))()

    const actionOnTickHandler = subscribe.mock.calls[0][1][1].subscription
    actionOnTickHandler({ body: JSON.stringify(12) })

    expect(dispatch).toHaveBeenCalledWith(
      actionOnTickReceived('game-1', 'table-1', 12),
    )
  })
})