import React from 'react'
import JoinGameDialog from './JoinGameDialog'
import { useDispatch, useSelector } from 'react-redux'
import { hideJoinGameModal, redirectToGame } from '../../../reducers'
import WebSocketService from '../../webSocket/WebSocketService'

const joinGameFormSubmitted = (hideDialogCallback, redirectToGame, gameId, evt) => {
  evt.preventDefault()
  WebSocketService.send('/app/joingame', gameId)
  hideDialogCallback()
  redirectToGame(gameId)
}

export default () => {
  const gameId = useSelector(state => state.joinGameId)
  const showModal = useSelector(state => state.showJoinGameModal)

  const dispatch = useDispatch()
  const dispatchHideJoinGameModal = () => dispatch(hideJoinGameModal())
  const dispatchRedirectToGame = gameId => dispatch(redirectToGame(gameId))

  return (
    <JoinGameDialog gameId={gameId}
                    showModal={showModal}
                    hideDialogCallback={dispatchHideJoinGameModal}
                    submitFormCallback={joinGameFormSubmitted.bind(null, dispatchHideJoinGameModal, dispatchRedirectToGame, gameId)} />
  )
}
