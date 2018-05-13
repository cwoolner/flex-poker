import WebSocketSubscriptionManager from '../webSocket/WebSocketSubscriptionManager'
import { pocketCardsReceived } from '../../reducers'

export default dispatch => {

  // used just as a placeholder for the websocket sub since there is no component
  const constObj = { unique: Math.random() }

  const displayPocketCards = message => {
    const parsedData = JSON.parse(message.body)
    const tableId = parsedData.tableId
    const pocketCards = {
      cardId1: parsedData.cardId1,
      cardId2: parsedData.cardId2
    }
    dispatch(pocketCardsReceived(tableId, pocketCards))
  }

  WebSocketSubscriptionManager.subscribe(constObj, [{
    location: '/user/queue/pocketcards',
    subscription: displayPocketCards
  }])

}
