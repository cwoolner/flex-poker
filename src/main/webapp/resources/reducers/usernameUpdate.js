export default (state = { username: 'UNKNOWN USERNAME' }, action) => {
  switch (action.type) {
    case 'UPDATE_USERNAME':
      return Object.assign({}, state, { username: action.username });
    default:
      return state;
  }
}
