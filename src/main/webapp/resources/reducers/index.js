const INIT_OPEN_GAME_TABS = 'INIT_OPEN_GAME_TABS'
const UPDATE_OPEN_GAME_TABS = 'UPDATE_OPEN_GAME_TABS'
const UPDATE_OPEN_GAME_LIST = 'UPDATE_OPEN_GAME_LIST'

export const initOpenGameTabs = openGameTabs => ({ type: INIT_OPEN_GAME_TABS, openGameTabs });
export const updateOpenGameTabs = openGameTabs => ({ type: UPDATE_OPEN_GAME_TABS, openGameTabs })
export const updateOpenGameList = openGameList => ({ type: UPDATE_OPEN_GAME_LIST, openGameList });

export default (state = {
  openGameTabs: [],
  openGameList: []
}, action) => {

  switch (action.type) {
    case INIT_OPEN_GAME_TABS:
      return Object.assign({}, state, { openGameTabs: action.openGameTabs });
    case UPDATE_OPEN_GAME_TABS:
      return Object.assign({}, state, { openGameTabs: action.openGameTabs });
    case UPDATE_OPEN_GAME_LIST:
      return Object.assign({}, state, { openGameList: action.openGameList });
    default:
      return state;
  }

}
