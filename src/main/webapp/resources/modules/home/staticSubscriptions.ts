import { Message } from 'webstomp-client'
import WebSocketSubscriptionManager from '../webSocket/WebSocketSubscriptionManager'
import { initOpenGameTabs, pocketCardsReceived, updateOpenGameList, updateOpenGameTabs, redirectToTable } from '../../reducers'

export default dispatch => {

  // used just as a placeholder for the websocket sub since there is no component
  const constObj = { unique: Math.random() }

  const displayPocketCards = (message: Message) => {
    const parsedData = JSON.parse(message.body)
    if (Array.isArray(parsedData)) {
      parsedData.forEach(x => {
        const handId = x.handId
        const pocketCards = {
          cardId1: x.cardId1,
          cardId2: x.cardId2
        }
        dispatch(pocketCardsReceived(handId, pocketCards))
      })
    } else {
      const handId = parsedData.handId
      const pocketCards = {
        cardId1: parsedData.cardId1,
        cardId2: parsedData.cardId2
      }
      dispatch(pocketCardsReceived(handId, pocketCards))
    }
  }

  const displayGameTabs = (message: Message) => {
    dispatch(initOpenGameTabs(JSON.parse(message.body)))
  }

  const openGameTab = (message: Message) => {
    dispatch(updateOpenGameTabs(JSON.parse(message.body)))
  }

  const openTable = (message: Message) => {
    const openTable = JSON.parse(message.body)
    dispatch(redirectToTable(openTable.gameId, openTable.tableId))
  }

  const updateGameList = (message: Message) => {
    dispatch(updateOpenGameList(JSON.parse(message.body)))
  }

  const displayUserError = (message: Message) => alert("Error " + message.body)

  const displayPersonalUserTableStatus = (message: Message) => alert(JSON.parse(message.body))

  WebSocketSubscriptionManager.subscribe(constObj, [
    { location: '/user/queue/pocketcards', subscription: displayPocketCards },
    { location: '/user/queue/errors', subscription: displayUserError },
    { location: '/app/opengamesforuser', subscription: displayGameTabs },
    { location: '/user/queue/opengamesforuser', subscription: openGameTab },
    { location: '/user/queue/opentable', subscription: openTable },
    { location: '/user/queue/personaltablestatus', subscription: displayPersonalUserTableStatus },
    { location: '/topic/availabletournaments', subscription: updateGameList }
  ])

}
