import React from 'react';
import JoinGameDialog from './JoinGameDialog'
import { connect } from 'react-redux'
import { hideJoinGameModal } from '../../../reducers'
import WebSocketService from '../../webSocket/WebSocketService';

const joinGameFormSubmitted = (hideDialogCallback, gameId, evt) => {
  evt.preventDefault()
  WebSocketService.send('/app/joingame', gameId)
  hideDialogCallback()
};

const JoinGameDialogContainer = ({gameId, showModal, dispatch}) => {
  const dispatchHideJoinGameModal = () => dispatch(hideJoinGameModal())

  return (
    <JoinGameDialog gameId={gameId}
                    showModal={showModal}
                    hideDialogCallback={dispatchHideJoinGameModal}
                    submitFormCallback={joinGameFormSubmitted.bind(null, dispatchHideJoinGameModal, gameId)} />
  )
}

const mapStateToProps = state => ({gameId: state.joinGameId, showModal: state.showJoinGameModal})

export default connect(mapStateToProps)(JoinGameDialogContainer)
