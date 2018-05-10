import WebSocketSubscriptionManager from '../../webSocket/WebSocketSubscriptionManager'
import { globalChatMsgReceived, gameChatMsgReceived, tableChatMsgReceived } from '../../../reducers'

// used just as a placeholder for the websocket sub since there is no component
const constObj = { unique: Math.random() }

const acceptGlobalChatMsg = (dispatch, message) =>
  dispatch(globalChatMsgReceived(JSON.parse(message.body)))

const acceptGameChatMsg = (dispatch, gameId, message) =>
  dispatch(gameChatMsgReceived(gameId, JSON.parse(message.body)))

const acceptTableChatMsg = (dispatch, gameId, tableId, message) =>
  dispatch(tableChatMsgReceived(gameId, tableId, JSON.parse(message.body)))

const registerWebSocketSubs = (dispatch, activeChatStream) => {
  const { gameId, tableId } = activeChatStream || {}
  if (gameId && tableId) {
    WebSocketSubscriptionManager.subscribe(constObj, [{
      location: `/topic/chat/game/${gameId}/table/${tableId}`,
      subscription: acceptTableChatMsg.bind(null, dispatch, gameId, tableId)
    }])
  } else if (gameId) {
    WebSocketSubscriptionManager.subscribe(constObj, [{
      location: `/topic/chat/game/${gameId}`,
      subscription: acceptGameChatMsg.bind(null, dispatch, gameId)
    }])
  } else {
    WebSocketSubscriptionManager.subscribe(constObj, [{
      location: '/topic/chat/global',
      subscription: acceptGlobalChatMsg.bind(null, dispatch)
    }])
  }
}

export default (store) => () => {
  registerWebSocketSubs(store.dispatch, store.getState().activeChatStream)
}
