const INIT_OPEN_GAME_TABS = 'INIT_OPEN_GAME_TABS'
const UPDATE_OPEN_GAME_TABS = 'UPDATE_OPEN_GAME_TABS'
const UPDATE_OPEN_GAME_LIST = 'UPDATE_OPEN_GAME_LIST'
const SHOW_JOIN_GAME_MODAL = 'SHOW_JOIN_GAME_MODAL'
const HIDE_JOIN_GAME_MODAL = 'HIDE_JOIN_GAME_MODAL'
const SHOW_CREATE_GAME_MODAL = 'SHOW_CREATE_GAME_MODAL'
const HIDE_CREATE_GAME_MODAL = 'HIDE_CREATE_GAME_MODAL'

export const initOpenGameTabs = openGameTabs => ({ type: INIT_OPEN_GAME_TABS, openGameTabs })
export const updateOpenGameTabs = openGameTabs => ({ type: UPDATE_OPEN_GAME_TABS, openGameTabs })
export const updateOpenGameList = openGameList => ({ type: UPDATE_OPEN_GAME_LIST, openGameList })
export const showJoinGameModal = joinGameId => ({ type: SHOW_JOIN_GAME_MODAL, joinGameId })
export const hideJoinGameModal = () => ({ type: HIDE_JOIN_GAME_MODAL })
export const showCreateGameModal = () => ({ type: SHOW_CREATE_GAME_MODAL })
export const hideCreateGameModal = () => ({ type: HIDE_CREATE_GAME_MODAL })

export default (state = {
  openGameTabs: [],
  openGameList: [],
  showJoinGameModal: false,
  joinGameId: null,
  showCreateGameModal: false
}, action) => {

  switch (action.type) {
    case INIT_OPEN_GAME_TABS:
      return Object.assign({}, state, { openGameTabs: action.openGameTabs })
    case UPDATE_OPEN_GAME_TABS:
      return Object.assign({}, state, { openGameTabs: action.openGameTabs })
    case UPDATE_OPEN_GAME_LIST:
      return Object.assign({}, state, { openGameList: action.openGameList })
    case SHOW_JOIN_GAME_MODAL:
      return Object.assign({}, state, { showJoinGameModal: true, joinGameId: action.joinGameId })
    case HIDE_JOIN_GAME_MODAL:
      return Object.assign({}, state, { showJoinGameModal: false, joinGameId: null })
    case SHOW_CREATE_GAME_MODAL:
      return Object.assign({}, state, { showCreateGameModal: true })
    case HIDE_CREATE_GAME_MODAL:
      return Object.assign({}, state, { showCreateGameModal: false })
    default:
      return state
  }

}
