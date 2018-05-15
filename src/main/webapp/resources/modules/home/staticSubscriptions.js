import WebSocketSubscriptionManager from '../webSocket/WebSocketSubscriptionManager'
import { initOpenGameTabs, pocketCardsReceived, updateOpenGameList, updateOpenGameTabs, redirectToTable } from '../../reducers'

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

  const displayGameTabs = message => {
    dispatch(initOpenGameTabs(JSON.parse(message.body)))
  }

  const openGameTab = message => {
    dispatch(updateOpenGameTabs(JSON.parse(message.body)))
  }

  const openTable = message => {
    const openTable = JSON.parse(message.body)
    dispatch(redirectToTable(openTable.gameId, openTable.tableId))
  }

  const updateGameList = message => {
    dispatch(updateOpenGameList(JSON.parse(message.body)))
  }

  WebSocketSubscriptionManager.subscribe(constObj, [
    { location: '/user/queue/pocketcards', subscription: displayPocketCards },
    { location: '/user/queue/errors', subscription: message => alert("Error " + message.body) },
    { location: '/app/opengamesforuser', subscription: displayGameTabs },
    { location: '/user/queue/opengamesforuser', subscription: openGameTab },
    { location: '/user/queue/opentable', subscription: openTable },
    { location: '/user/queue/personaltablestatus', subscription: message => alert(JSON.parse(message.body)) },
    { location: '/topic/availabletournaments', subscription: updateGameList }
  ])

}
