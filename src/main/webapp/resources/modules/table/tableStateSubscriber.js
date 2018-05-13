import { createSelector } from 'reselect'
import WebSocketSubscriptionManager from '../webSocket/WebSocketSubscriptionManager'
import { tableUpdateReceived } from '../../reducers'

export default dispatch => {

  // used just as a placeholder for the websocket sub since there is no component
  const constObj = { unique: Math.random() }

  const activeTable = state => state.activeTable

  const activeTableSelector = createSelector([ activeTable ],
    activeTable => registerWebSocketSubs(activeTable))

  const acceptTableData = (gameId, tableId, message) =>
    dispatch(tableUpdateReceived(gameId, tableId, JSON.parse(message.body)))

  const registerWebSocketSubs = activeTable => {
    const { gameId, tableId } = activeTable || {}
    if (gameId && tableId) {
      WebSocketSubscriptionManager.subscribe(constObj, [{
        location: `/topic/game/${gameId}/table/${tableId}`,
        subscription: acceptTableData.bind(null, gameId, tableId)
      }])
    }
  }

  return store => () => {
    activeTableSelector(store.getState())
  }

}
