import {
  INIT_OPEN_GAME_TABS,
  UPDATE_OPEN_GAME_TABS,
  UPDATE_OPEN_GAME_LIST
} from '../constants/ActionTypes';

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
