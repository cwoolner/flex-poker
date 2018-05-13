import { List, Map } from 'immutable'

const INIT_OPEN_GAME_TABS = 'INIT_OPEN_GAME_TABS'
const UPDATE_OPEN_GAME_TABS = 'UPDATE_OPEN_GAME_TABS'
const UPDATE_OPEN_GAME_LIST = 'UPDATE_OPEN_GAME_LIST'
const SHOW_JOIN_GAME_MODAL = 'SHOW_JOIN_GAME_MODAL'
const HIDE_JOIN_GAME_MODAL = 'HIDE_JOIN_GAME_MODAL'
const SHOW_CREATE_GAME_MODAL = 'SHOW_CREATE_GAME_MODAL'
const HIDE_CREATE_GAME_MODAL = 'HIDE_CREATE_GAME_MODAL'
const CHANGE_CHAT_MSG_STREAM = 'CHANGE_CHAT_MSG_STREAM'
const GLOBAL_CHAT_MSG_RECEIVED = 'GLOBAL_CHAT_MSG_RECEIVED'
const GAME_CHAT_MSG_RECEIVED = 'GAME_CHAT_MSG_RECEIVED'
const TABLE_CHAT_MSG_RECEIVED = 'TABLE_CHAT_MSG_RECEIVED'
const CHANGE_TABLE = 'CHANGE_TABLE'
const TABLE_UPDATE_RECEIVED = 'TABLE_UPDATE_RECEIVED'
const POCKET_CARDS_RECEIVED = 'POCKET_CARDS_RECEIVED'

export const initOpenGameTabs = openGameTabs => ({ type: INIT_OPEN_GAME_TABS, openGameTabs })
export const updateOpenGameTabs = openGameTabs => ({ type: UPDATE_OPEN_GAME_TABS, openGameTabs })
export const updateOpenGameList = openGameList => ({ type: UPDATE_OPEN_GAME_LIST, openGameList })
export const showJoinGameModal = joinGameId => ({ type: SHOW_JOIN_GAME_MODAL, joinGameId })
export const hideJoinGameModal = () => ({ type: HIDE_JOIN_GAME_MODAL })
export const showCreateGameModal = () => ({ type: SHOW_CREATE_GAME_MODAL })
export const hideCreateGameModal = () => ({ type: HIDE_CREATE_GAME_MODAL })
export const changeChatMsgStream = (gameId, tableId) => ({ type: CHANGE_CHAT_MSG_STREAM, gameId, tableId })
export const globalChatMsgReceived = msg => ({ type: GLOBAL_CHAT_MSG_RECEIVED, chatMessage: msg })
export const gameChatMsgReceived = (gameId, msg) => ({ type: GAME_CHAT_MSG_RECEIVED, gameId, chatMessage: msg })
export const tableChatMsgReceived = (gameId, tableId, msg) => ({ type: TABLE_CHAT_MSG_RECEIVED, gameId, tableId, chatMessage: msg })
export const changeTable = (gameId, tableId) => ({ type: CHANGE_TABLE, gameId, tableId })
export const tableUpdateReceived = (gameId, tableId, tableState) => ({ type: TABLE_UPDATE_RECEIVED, gameId, tableId, tableState })
export const pocketCardsReceived = (tableId, pocketCards) => ({ type: POCKET_CARDS_RECEIVED, tableId, pocketCards })

export default (state = {
  openGameTabs: [],
  openGameList: [],
  showJoinGameModal: false,
  joinGameId: null,
  showCreateGameModal: false,
  activeChatStream: { gameId: null, tableId: null },
  chatMessages: {
    globalMessages: List(),
    gameMessages: Map(),
    tableMessages: Map()
  },
  activeTable: { gameId: null, tableId: null },
  tables: Map(),
  pocketCards: Map()
}, action) => {

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
    case GLOBAL_CHAT_MSG_RECEIVED:
      const globalMessages = state.chatMessages.globalMessages.push(action.chatMessage)
      return { ...state, chatMessages: { ...state.chatMessages, globalMessages }}
    case GAME_CHAT_MSG_RECEIVED:
      const singleGameMessages = state.chatMessages.gameMessages.get(action.gameId, List()).push(action.chatMessage)
      const gameMessages = state.chatMessages.gameMessages.set(action.gameId, singleGameMessages)
      return { ...state, chatMessages: { ...state.chatMessages, gameMessages }}
    case TABLE_CHAT_MSG_RECEIVED:
      const singleTableMessages = state.chatMessages.tableMessages.get(action.tableId, List()).push(action.chatMessage)
      const tableMessages = state.chatMessages.tableMessages.set(action.tableId, singleTableMessages)
      return { ...state, chatMessages: { ...state.chatMessages, tableMessages }}
    case CHANGE_TABLE:
      return { ...state, activeTable: { gameId: action.gameId, tableId: action.tableId }}
    case TABLE_UPDATE_RECEIVED:
      const currentVersion = state.tables.get(action.gameId, Map()).get(action.tableId, {}).version || 0
      const updatedVersion = action.tableState.version
      if (updatedVersion > currentVersion) {
        const gameTables = state.tables.get(action.gameId, Map()).set(action.tableId, action.tableState)
        const updatedTables = state.tables.set(action.gameId, gameTables)
        return { ...state, tables: updatedTables }
      } else {
        return state
      }
    case POCKET_CARDS_RECEIVED:
      return { ...state, pocketCards: state.pocketCards.set(action.tableId, action.pocketCards)}
    default:
      return state
  }

}
