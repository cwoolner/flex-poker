import React from 'react'
import JoinGameDialog from './JoinGameDialog'
import { connect } from 'react-redux'
import { hideJoinGameModal, redirectToGame } from '../../../reducers'
import WebSocketService from '../../webSocket/WebSocketService'

const joinGameFormSubmitted = (hideDialogCallback, redirectToGame, gameId, evt) => {
  evt.preventDefault()
  WebSocketService.send('/app/joingame', gameId)
  hideDialogCallback()
  redirectToGame(gameId)
}

const JoinGameDialogContainer = ({gameId, showModal, hideJoinGameModal, redirectToGame}) => {
  return (
    <JoinGameDialog gameId={gameId}
                    showModal={showModal}
                    hideDialogCallback={hideJoinGameModal}
                    submitFormCallback={joinGameFormSubmitted.bind(null, hideJoinGameModal, redirectToGame, gameId)} />
  )
}

const mapStateToProps = state => ({gameId: state.joinGameId, showModal: state.showJoinGameModal})

const mapDispatchToProps = dispatch => ({
  hideJoinGameModal: () => dispatch(hideJoinGameModal()),
  redirectToGame: gameId => dispatch(redirectToGame(gameId))
})

export default connect(mapStateToProps, mapDispatchToProps)(JoinGameDialogContainer)
