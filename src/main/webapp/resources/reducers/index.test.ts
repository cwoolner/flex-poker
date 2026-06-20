import { describe, expect, it } from 'vitest'
import { List } from 'immutable'
import reducer, {
  actionOnTickReceived,
  changeChatMsgStream,
  changeTable,
  clearRedirect,
  gameChatMsgReceived,
  hideCreateGameModal,
  hideJoinGameModal,
  initOpenGameTabs,
  lobbyChatMsgReceived,
  loginUser,
  logoutUser,
  pocketCardsReceived,
  redirectToGame,
  redirectToTable,
  showCreateGameModal,
  showJoinGameModal,
  tableChatMsgReceived,
  tableUpdateReceived,
  updateOpenGameList,
  updateOpenGameTabs,
} from './index'
import { INITITAL_APP_STATE } from './types'

describe('reducer', () => {
  it('returns the initial state for unknown actions', () => {
    expect(reducer(undefined, { type: 'UNKNOWN' })).toEqual(INITITAL_APP_STATE)
  })

  describe('user session', () => {
    it('logs in a user', () => {
      const state = reducer(INITITAL_APP_STATE, loginUser('player1'))

      expect(state.userInfo).toEqual({ username: 'player1', loggedIn: true })
    })

    it('logs out a user', () => {
      const loggedInState = reducer(INITITAL_APP_STATE, loginUser('player1'))
      const state = reducer(loggedInState, logoutUser())

      expect(state.userInfo).toEqual({ username: null, loggedIn: false })
    })
  })

  describe('lobby modals', () => {
    it('shows and hides the join game modal', () => {
      const shown = reducer(INITITAL_APP_STATE, showJoinGameModal('game-1'))

      expect(shown.showJoinGameModal).toBe(true)
      expect(shown.joinGameId).toBe('game-1')

      const hidden = reducer(shown, hideJoinGameModal())

      expect(hidden.showJoinGameModal).toBe(false)
      expect(hidden.joinGameId).toBe(null)
    })

    it('shows and hides the create game modal', () => {
      const shown = reducer(INITITAL_APP_STATE, showCreateGameModal())

      expect(shown.showCreateGameModal).toBe(true)

      const hidden = reducer(shown, hideCreateGameModal())

      expect(hidden.showCreateGameModal).toBe(false)
    })
  })

  describe('open games', () => {
    it('updates open game tabs and list', () => {
      const openGameTabs = [{ gameId: 'game-1' }]
      const withTabs = reducer(INITITAL_APP_STATE, initOpenGameTabs(openGameTabs))
      const updatedTabs = [{ gameId: 'game-1' }, { gameId: 'game-2' }]
      const withUpdatedTabs = reducer(withTabs, updateOpenGameTabs(updatedTabs))

      expect(withTabs.openGameTabs).toEqual(openGameTabs)
      expect(withUpdatedTabs.openGameTabs).toEqual(updatedTabs)

      const openGameList = [{ gameId: 'game-1', name: 'Friday Night' }]
      const withList = reducer(INITITAL_APP_STATE, updateOpenGameList(openGameList))

      expect(withList.openGameList).toEqual(openGameList)
    })
  })

  describe('navigation', () => {
    it('sets redirect urls and clears them', () => {
      const toGame = reducer(INITITAL_APP_STATE, redirectToGame('game-1'))
      const toTable = reducer(INITITAL_APP_STATE, redirectToTable('game-1', 'table-1'))
      const cleared = reducer(toTable, clearRedirect())

      expect(toGame.redirectUrl).toBe('/game/game-1')
      expect(toTable.redirectUrl).toBe('/game/game-1/table/table-1')
      expect(cleared.redirectUrl).toBe(null)
    })

    it('changes the active table', () => {
      const state = reducer(INITITAL_APP_STATE, changeTable('game-1', 'table-1'))

      expect(state.activeTable).toEqual({ gameId: 'game-1', tableId: 'table-1' })
    })
  })

  describe('chat messages', () => {
    it('changes the active chat stream', () => {
      const state = reducer(INITITAL_APP_STATE, changeChatMsgStream('game-1', 'table-1'))

      expect(state.activeChatStream).toEqual({ gameId: 'game-1', tableId: 'table-1' })
    })

    it('appends lobby chat messages', () => {
      const first = reducer(INITITAL_APP_STATE, lobbyChatMsgReceived('hello'))
      const second = reducer(first, lobbyChatMsgReceived('world'))

      expect(second.chatMessages.lobbyMessages).toEqual(List(['hello', 'world']))
    })

    it('replaces lobby chat messages when given an array', () => {
      const state = reducer(INITITAL_APP_STATE, lobbyChatMsgReceived(['one', 'two']))

      expect(state.chatMessages.lobbyMessages).toEqual(List(['one', 'two']))
    })

    it('appends game chat messages per game', () => {
      const gameOne = reducer(INITITAL_APP_STATE, gameChatMsgReceived('game-1', 'hi'))
      const bothGames = reducer(gameOne, gameChatMsgReceived('game-2', 'yo'))

      expect(bothGames.chatMessages.gameMessages.get('game-1')).toEqual(List(['hi']))
      expect(bothGames.chatMessages.gameMessages.get('game-2')).toEqual(List(['yo']))
    })

    it('appends table chat messages per table', () => {
      const tableOne = reducer(INITITAL_APP_STATE, tableChatMsgReceived('game-1', 'table-1', 'nice hand'))
      const bothTables = reducer(tableOne, tableChatMsgReceived('game-1', 'table-2', 'gg'))

      expect(bothTables.chatMessages.tableMessages.get('table-1')).toEqual(List(['nice hand']))
      expect(bothTables.chatMessages.tableMessages.get('table-2')).toEqual(List(['gg']))
    })
  })

  describe('table state', () => {
    const tableStateV1 = {
      id: 'table-1',
      version: 1,
      seats: [],
      totalPot: 100,
      pots: [],
      visibleCommonCards: [],
      currentHandMinRaiseToAmount: 20,
      currentHandId: 'hand-1',
    }

    const tableStateV2 = { ...tableStateV1, version: 2, totalPot: 200 }

    it('stores a table update when the version increases', () => {
      const state = reducer(
        INITITAL_APP_STATE,
        tableUpdateReceived('game-1', 'table-1', tableStateV1),
      )

      expect(state.tables.get('game-1')?.get('table-1')).toEqual(tableStateV1)
    })

    it('ignores stale or duplicate table updates', () => {
      const withV2 = reducer(
        INITITAL_APP_STATE,
        tableUpdateReceived('game-1', 'table-1', tableStateV2),
      )
      const unchanged = reducer(
        withV2,
        tableUpdateReceived('game-1', 'table-1', tableStateV1),
      )

      expect(unchanged).toBe(withV2)
      expect(unchanged.tables.get('game-1')?.get('table-1')).toEqual(tableStateV2)
    })

    it('stores action-on ticks per game and table', () => {
      const state = reducer(
        INITITAL_APP_STATE,
        actionOnTickReceived('game-1', 'table-1', 12),
      )

      expect(state.actionOnTicks.get('game-1')?.get('table-1')).toBe(12)
    })

    it('stores pocket cards per hand', () => {
      const pocketCards = { handId: 'hand-1', cardId1: 10, cardId2: 22 }
      const state = reducer(
        INITITAL_APP_STATE,
        pocketCardsReceived('hand-1', pocketCards),
      )

      expect(state.pocketCards.get('hand-1')).toEqual(pocketCards)
    })
  })
})