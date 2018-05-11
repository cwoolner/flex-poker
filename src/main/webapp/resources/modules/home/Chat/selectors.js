import { createSelector } from 'reselect'
import { List } from 'immutable'

const getActiveChatStream = state => state.activeChatStream

const getChatMessages = state => state.chatMessages

export const getChats = createSelector(
  [ getActiveChatStream, getChatMessages ],
  (activeChatStream, chatMessages) => {
    if (activeChatStream.gameId && activeChatStream.tableId) {
      const { gameId, tableId } = activeChatStream
      return chatMessages.tableMessages.get(tableId, List())
    } else if (activeChatStream.gameId) {
      const { gameId } = activeChatStream
      return chatMessages.gameMessages.get(gameId, List())
    } else {
      return chatMessages.globalMessages
    }
  }
)
