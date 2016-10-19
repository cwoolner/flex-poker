export default (state = { username: 'UNKNOWN USERNAME', openGameTabs: [] }, action) => {
  switch (action.type) {
    case 'UPDATE_USERNAME':
      return Object.assign({}, state, { username: action.username });
    case 'INIT_OPEN_GAME_TABS':
    case 'UPDATE_OPEN_GAME_TABS':
      return Object.assign({}, state, { openGameTabs: action.openGameTabs });
    default:
      return state;
  }
}
