import { createSelector } from 'reselect'
import WebSocketSubscriptionManager from '../../webSocket/WebSocketSubscriptionManager'
import { globalChatMsgReceived, gameChatMsgReceived, tableChatMsgReceived } from '../../../reducers'

export default dispatch => {

  // used just as a placeholder for the websocket sub since there is no component
  const constObj = { unique: Math.random() }

  const activeChatStreamState = state => state.activeChatStream

  const activeChatStreamSelector = createSelector([ activeChatStreamState ],
    activeChatStream => registerWebSocketSubs(activeChatStream))

  const acceptGlobalChatMsg = message =>
    dispatch(globalChatMsgReceived(JSON.parse(message.body)))

  const acceptGameChatMsg = (gameId, message) =>
    dispatch(gameChatMsgReceived(gameId, JSON.parse(message.body)))

  const acceptTableChatMsg = (gameId, tableId, message) =>
    dispatch(tableChatMsgReceived(gameId, tableId, JSON.parse(message.body)))

  const registerWebSocketSubs = activeChatStream => {
    const { gameId, tableId } = activeChatStream || {}
    if (gameId && tableId) {
      WebSocketSubscriptionManager.subscribe(constObj, [{
        location: `/topic/chat/game/${gameId}/table/${tableId}`,
        subscription: acceptTableChatMsg.bind(null, gameId, tableId)
      }])
    } else if (gameId) {
      WebSocketSubscriptionManager.subscribe(constObj, [{
        location: `/topic/chat/game/${gameId}`,
        subscription: acceptGameChatMsg.bind(null, gameId)
      }])
    } else {
      WebSocketSubscriptionManager.subscribe(constObj, [{
        location: '/topic/chat/global',
        subscription: acceptGlobalChatMsg
      }])
    }
  }

  return store => () => {
    activeChatStreamSelector(store.getState())
  }

}
