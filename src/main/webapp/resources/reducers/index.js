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

export const initOpenGameTabs = openGameTabs => ({ type: INIT_OPEN_GAME_TABS, openGameTabs })
export const updateOpenGameTabs = openGameTabs => ({ type: UPDATE_OPEN_GAME_TABS, openGameTabs })
export const updateOpenGameList = openGameList => ({ type: UPDATE_OPEN_GAME_LIST, openGameList })
export const showJoinGameModal = joinGameId => ({ type: SHOW_JOIN_GAME_MODAL, joinGameId })
export const hideJoinGameModal = () => ({ type: HIDE_JOIN_GAME_MODAL })
export const showCreateGameModal = () => ({ type: SHOW_CREATE_GAME_MODAL })
export const hideCreateGameModal = () => ({ type: HIDE_CREATE_GAME_MODAL })
export const changeChatMsgStream = (gameId, tableId) => ({ type: CHANGE_CHAT_MSG_STREAM, gameId, tableId })
export const globalChatMsgReceived = msg => ({ type: GLOBAL_CHAT_MSG_RECEIVED, chatMessage: msg })
export const gameChatMsgReceived = msg => ({ type: GAME_CHAT_MSG_RECEIVED, chatMessage: msg })
export const tableChatMsgReceived = msg => ({ type: TABLE_CHAT_MSG_RECEIVED, chatMessage: msg })

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
  }
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
    default:
      return state
  }

}
