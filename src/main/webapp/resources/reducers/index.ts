import { List, Map } from 'immutable'
import { DEFAULT_GAME, DEFAULT_TABLE, INITITAL_APP_STATE } from './types'

const INIT_OPEN_GAME_TABS = 'INIT_OPEN_GAME_TABS'
const UPDATE_OPEN_GAME_TABS = 'UPDATE_OPEN_GAME_TABS'
const UPDATE_OPEN_GAME_LIST = 'UPDATE_OPEN_GAME_LIST'
const SHOW_JOIN_GAME_MODAL = 'SHOW_JOIN_GAME_MODAL'
const HIDE_JOIN_GAME_MODAL = 'HIDE_JOIN_GAME_MODAL'
const SHOW_CREATE_GAME_MODAL = 'SHOW_CREATE_GAME_MODAL'
const HIDE_CREATE_GAME_MODAL = 'HIDE_CREATE_GAME_MODAL'
const CHANGE_CHAT_MSG_STREAM = 'CHANGE_CHAT_MSG_STREAM'
const LOBBY_CHAT_MSG_RECEIVED = 'LOBBY_CHAT_MSG_RECEIVED'
const GAME_CHAT_MSG_RECEIVED = 'GAME_CHAT_MSG_RECEIVED'
const TABLE_CHAT_MSG_RECEIVED = 'TABLE_CHAT_MSG_RECEIVED'
const CHANGE_TABLE = 'CHANGE_TABLE'
const TABLE_UPDATE_RECEIVED = 'TABLE_UPDATE_RECEIVED'
const ACTION_ON_TICK_RECEIVED = 'ACTION_ON_TICK_RECEIVED'
const POCKET_CARDS_RECEIVED = 'POCKET_CARDS_RECEIVED'
const REDIRECT_TO_GAME = 'REDIRECT_TO_GAME'
const REDIRECT_TO_TABLE = 'REDIRECT_TO_TABLE'
const CLEAR_REDIRECT = 'CLEAR_REDIRECT'
const LOGIN_USER = 'LOGIN_USER'
const LOGOUT_USER = 'LOGOUT_USER'

export const initOpenGameTabs = openGameTabs => ({ type: INIT_OPEN_GAME_TABS, openGameTabs })
export const updateOpenGameTabs = openGameTabs => ({ type: UPDATE_OPEN_GAME_TABS, openGameTabs })
export const updateOpenGameList = openGameList => ({ type: UPDATE_OPEN_GAME_LIST, openGameList })
export const showJoinGameModal = joinGameId => ({ type: SHOW_JOIN_GAME_MODAL, joinGameId })
export const hideJoinGameModal = () => ({ type: HIDE_JOIN_GAME_MODAL })
export const showCreateGameModal = () => ({ type: SHOW_CREATE_GAME_MODAL })
export const hideCreateGameModal = () => ({ type: HIDE_CREATE_GAME_MODAL })
export const changeChatMsgStream = (gameId, tableId) => ({ type: CHANGE_CHAT_MSG_STREAM, gameId, tableId })
export const lobbyChatMsgReceived = msg => ({ type: LOBBY_CHAT_MSG_RECEIVED, chatMessage: msg })
export const gameChatMsgReceived = (gameId, msg) => ({ type: GAME_CHAT_MSG_RECEIVED, gameId, chatMessage: msg })
export const tableChatMsgReceived = (gameId, tableId, msg) => ({ type: TABLE_CHAT_MSG_RECEIVED, gameId, tableId, chatMessage: msg })
export const changeTable = (gameId, tableId) => ({ type: CHANGE_TABLE, gameId, tableId })
export const tableUpdateReceived = (gameId, tableId, tableState) => ({ type: TABLE_UPDATE_RECEIVED, gameId, tableId, tableState })
export const actionOnTickReceived = (gameId, tableId, actionOnTick) => ({ type: ACTION_ON_TICK_RECEIVED, gameId, tableId, actionOnTick })
export const pocketCardsReceived = (handId, pocketCards) => ({ type: POCKET_CARDS_RECEIVED, handId, pocketCards })
export const redirectToGame = gameId => ({ type: REDIRECT_TO_GAME, gameId })
export const redirectToTable = (gameId, tableId) => ({ type: REDIRECT_TO_TABLE, gameId, tableId })
export const clearRedirect = () => ({ type: CLEAR_REDIRECT })
export const loginUser = username => ({ type: LOGIN_USER, username })
export const logoutUser = () => ({ type: LOGOUT_USER })

export default (state = INITITAL_APP_STATE, action) => {
  switch (action.type) {
    case INIT_OPEN_GAME_TABS:
      return { ...state, openGameTabs: action.openGameTabs }
    case UPDATE_OPEN_GAME_TABS:
      return { ...state, openGameTabs: action.openGameTabs }
    case UPDATE_OPEN_GAME_LIST:
      return { ...state, openGameList: action.openGameList }
    case SHOW_JOIN_GAME_MODAL:
      return { ...state, showJoinGameModal: true, joinGameId: action.joinGameId }
    case HIDE_JOIN_GAME_MODAL:
      return { ...state, showJoinGameModal: false, joinGameId: null }
    case SHOW_CREATE_GAME_MODAL:
      return {...state, showCreateGameModal: true }
    case HIDE_CREATE_GAME_MODAL:
      return { ...state, showCreateGameModal: false }
    case CHANGE_CHAT_MSG_STREAM:
      return { ...state, activeChatStream: { gameId: action.gameId, tableId: action.tableId }}
    case LOBBY_CHAT_MSG_RECEIVED:
      const lobbyMessages = Array.isArray(action.chatMessage)
          ? List(action.chatMessage)
          : state.chatMessages.lobbyMessages.push(action.chatMessage)
      return { ...state, chatMessages: { ...state.chatMessages, lobbyMessages }}
    case GAME_CHAT_MSG_RECEIVED:
      const singleGameMessages: List<string> = Array.isArray(action.chatMessage)
          ? List(action.chatMessage)
          : state.chatMessages.gameMessages.get(action.gameId, List<string>()).push(action.chatMessage)
      const gameMessages = state.chatMessages.gameMessages.set(action.gameId, singleGameMessages)
      return { ...state, chatMessages: { ...state.chatMessages, gameMessages }}
    case TABLE_CHAT_MSG_RECEIVED:
      const singleTableMessages: List<string> = Array.isArray(action.chatMessage)
          ? List(action.chatMessage)
          : state.chatMessages.tableMessages.get(action.tableId, List<string>()).push(action.chatMessage)
      const tableMessages = state.chatMessages.tableMessages.set(action.tableId, singleTableMessages)
      return { ...state, chatMessages: { ...state.chatMessages, tableMessages }}
    case CHANGE_TABLE:
      return { ...state, activeTable: { gameId: action.gameId, tableId: action.tableId }}
    case TABLE_UPDATE_RECEIVED:
      const currentVersion = state.tables.get(action.gameId, DEFAULT_GAME).get(action.tableId, DEFAULT_TABLE).version || 0
      const updatedVersion = action.tableState.version
      if (updatedVersion > currentVersion) {
        const gameTables: Map<string, any> = state.tables.get(action.gameId, DEFAULT_GAME).set(action.tableId, action.tableState)
        const updatedTables = state.tables.set(action.gameId, gameTables)
        return { ...state, tables: updatedTables }
      } else {
        return state
      }
    case ACTION_ON_TICK_RECEIVED:
      const gameTicks = state.actionOnTicks.get(action.gameId, Map()).set(action.tableId, action.actionOnTick)
      const updatedActionOnTicks = state.actionOnTicks.set(action.gameId, gameTicks)
      return { ...state, actionOnTicks: updatedActionOnTicks }
    case POCKET_CARDS_RECEIVED:
      return { ...state, pocketCards: state.pocketCards.set(action.handId, action.pocketCards)}
    case REDIRECT_TO_GAME:
      return { ...state, redirectUrl: `/game/${action.gameId}` }
    case REDIRECT_TO_TABLE:
      return { ...state, redirectUrl: `/game/${action.gameId}/table/${action.tableId}` }
    case CLEAR_REDIRECT:
      return { ...state, redirectUrl: null }
    case LOGIN_USER:
      return { ...state, userInfo: { username: action.username, loggedIn: true }}
    case LOGOUT_USER:
      return { ...state, userInfo: { username: null, loggedIn: false }}
    default:
      return state
  }

}
