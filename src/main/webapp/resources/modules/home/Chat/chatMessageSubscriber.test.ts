import { beforeEach, describe, expect, it, vi } from 'vitest'
import { INITITAL_APP_STATE } from '../../../reducers/types'
import {
  gameChatMsgReceived,
  lobbyChatMsgReceived,
  tableChatMsgReceived,
} from '../../../reducers'

const { subscribe } = vi.hoisted(() => ({
  subscribe: vi.fn(),
}))

vi.mock('../../webSocket/WebSocketSubscriptionManager', () => ({
  default: { subscribe },
}))

import chatMessageSubscriber from './chatMessageSubscriber'

const createStore = (activeChatStream: { gameId: string | null, tableId: string | null }) => ({
  getState: () => ({
    ...INITITAL_APP_STATE,
    activeChatStream,
  }),
})

describe('chatMessageSubscriber', () => {
  beforeEach(() => {
    subscribe.mockClear()
  })

  it('subscribes to lobby chat when no game is active', () => {
    const dispatch = vi.fn()
    const onStoreChange = chatMessageSubscriber(dispatch)

    onStoreChange(createStore({ gameId: null, tableId: null }))()

    expect(subscribe).toHaveBeenCalledOnce()
    expect(subscribe.mock.calls[0][1]).toEqual([{
      location: '/topic/chat/lobby',
      subscription: expect.any(Function),
    }])
  })

  it('subscribes to game chat when only a game is active', () => {
    const dispatch = vi.fn()
    const onStoreChange = chatMessageSubscriber(dispatch)

    onStoreChange(createStore({ gameId: 'game-1', tableId: null }))()

    expect(subscribe).toHaveBeenCalledOnce()
    expect(subscribe.mock.calls[0][1]).toEqual([{
      location: '/topic/chat/game/game-1',
      subscription: expect.any(Function),
    }])
  })

  it('subscribes to table chat when both game and table are active', () => {
    const dispatch = vi.fn()
    const onStoreChange = chatMessageSubscriber(dispatch)

    onStoreChange(createStore({ gameId: 'game-1', tableId: 'table-1' }))()

    expect(subscribe).toHaveBeenCalledOnce()
    expect(subscribe.mock.calls[0][1]).toEqual([{
      location: '/topic/chat/game/game-1/table/table-1',
      subscription: expect.any(Function),
    }])
  })

  it('dispatches lobby chat messages from websocket payloads', () => {
    const dispatch = vi.fn()
    const onStoreChange = chatMessageSubscriber(dispatch)

    onStoreChange(createStore({ gameId: null, tableId: null }))()

    const handler = subscribe.mock.calls[0][1][0].subscription
    handler({ body: JSON.stringify({ message: 'hello lobby', senderUsername: 'player1' }) })

    expect(dispatch).toHaveBeenCalledWith(
      lobbyChatMsgReceived({ message: 'hello lobby', senderUsername: 'player1' }),
    )
  })

  it('dispatches game chat messages from websocket payloads', () => {
    const dispatch = vi.fn()
    const onStoreChange = chatMessageSubscriber(dispatch)

    onStoreChange(createStore({ gameId: 'game-1', tableId: null }))()

    const handler = subscribe.mock.calls[0][1][0].subscription
    handler({ body: JSON.stringify({ message: 'hello game', senderUsername: 'player2' }) })

    expect(dispatch).toHaveBeenCalledWith(
      gameChatMsgReceived('game-1', { message: 'hello game', senderUsername: 'player2' }),
    )
  })

  it('dispatches table chat messages from websocket payloads', () => {
    const dispatch = vi.fn()
    const onStoreChange = chatMessageSubscriber(dispatch)

    onStoreChange(createStore({ gameId: 'game-1', tableId: 'table-1' }))()

    const handler = subscribe.mock.calls[0][1][0].subscription
    handler({ body: JSON.stringify({ message: 'nice hand', senderUsername: 'player3' }) })

    expect(dispatch).toHaveBeenCalledWith(
      tableChatMsgReceived('game-1', 'table-1', { message: 'nice hand', senderUsername: 'player3' }),
    )
  })
})