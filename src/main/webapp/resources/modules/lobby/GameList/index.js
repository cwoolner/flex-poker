import React from 'react'
import GameList from './GameList'
import { useSelector, useDispatch } from 'react-redux'
import { showJoinGameModal, showCreateGameModal } from '../../../reducers'

export default () => {
  const dispatch = useDispatch()
  const gameList = useSelector(state => state.openGameList)

  return (
    <GameList gameList={gameList}
              gameOpenedCallback={joinGameId => dispatch(showJoinGameModal(joinGameId))}
              openCreateGameModalCallback={() => dispatch(showCreateGameModal())} />
  )
}
