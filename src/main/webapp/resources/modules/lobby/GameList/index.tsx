import React from 'react'
import GameList from './GameList'
import { useSelector, useDispatch } from 'react-redux'
import { showJoinGameModal, showCreateGameModal } from '../../../reducers'
import { RootState } from '../../..'

export default () => {
  const dispatch = useDispatch()
  const gameList = useSelector((state: RootState) => state.openGameList)

  return (
    <GameList gameList={gameList}
              gameOpenedCallback={joinGameId => dispatch(showJoinGameModal(joinGameId))}
              openCreateGameModalCallback={() => dispatch(showCreateGameModal())} />
  )
}
