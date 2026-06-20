import { describe, expect, it } from 'vitest'
import { List, Map } from 'immutable'
import { getChats } from './selectors'
import { INITITAL_APP_STATE } from '../../../reducers/types'

const lobbyMessages = List(['lobby: hello'])
const gameMessages = Map({ 'game-1': List(['game: hi']) })
const tableMessages = Map({ 'table-1': List(['table: nice hand']) })

const stateWithChatMessages = {
  ...INITITAL_APP_STATE,
  chatMessages: {
    lobbyMessages,
    gameMessages,
    tableMessages,
  },
}

describe('getChats', () => {
  it('returns lobby messages when no game is active', () => {
    const state = {
      ...stateWithChatMessages,
      activeChatStream: { gameId: null, tableId: null },
    }

    expect(getChats(state)).toBe(lobbyMessages)
  })

  it('returns game messages when a game is active but no table is selected', () => {
    const state = {
      ...stateWithChatMessages,
      activeChatStream: { gameId: 'game-1', tableId: null },
    }

    expect(getChats(state)).toBe(gameMessages.get('game-1'))
  })

  it('returns an empty list for an unknown game', () => {
    const state = {
      ...stateWithChatMessages,
      activeChatStream: { gameId: 'missing-game', tableId: null },
    }

    expect(getChats(state)).toEqual(List())
  })

  it('returns table messages when both game and table are active', () => {
    const state = {
      ...stateWithChatMessages,
      activeChatStream: { gameId: 'game-1', tableId: 'table-1' },
    }

    expect(getChats(state)).toBe(tableMessages.get('table-1'))
  })

  it('returns an empty list for an unknown table', () => {
    const state = {
      ...stateWithChatMessages,
      activeChatStream: { gameId: 'game-1', tableId: 'missing-table' },
    }

    expect(getChats(state)).toEqual(List())
  })

  it('prefers table chat over game chat when both ids are set', () => {
    const state = {
      ...stateWithChatMessages,
      activeChatStream: { gameId: 'game-1', tableId: 'table-1' },
    }

    expect(getChats(state)).not.toBe(gameMessages.get('game-1'))
    expect(getChats(state)).toBe(tableMessages.get('table-1'))
  })
})