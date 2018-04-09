import React from 'react';
import WebSocketService from '../../webSocket/WebSocketService';
import { connect } from 'react-redux';
import { hideCreateGameModal } from '../../../reducers'
import CreateGameDialog from './CreateGameDialog'

const createGameFormSubmitted = (hideDialogCallback, evt) => {
    evt.preventDefault()

    WebSocketService.send('/app/creategame', {
      name: evt.target.elements.name.value,
      players: evt.target.elements.players.value,
      playersPerTable: evt.target.elements.playersPerTable.value,
      numberOfMinutesBetweenBlindLevels: evt.target.elements.numberOfMinutesBetweenBlindLevels.value,
      numberOfSecondsForActionOnTimer: evt.target.elements.secondsForActionOnTimer.value
    })

    hideDialogCallback()
}

const CreateGameDialogContainer = ({showModal, dispatch}) => {
  const dispatchHideCreateGameModal = () => dispatch(hideCreateGameModal())

  return (
    <CreateGameDialog showModal={showModal}
                      hideDialogCallback={dispatchHideCreateGameModal}
                      submitFormCallback={createGameFormSubmitted.bind(null, dispatchHideCreateGameModal)} />
  )
}

const mapStateToProps = state => ({showModal: state.showCreateGameModal})

export default connect(mapStateToProps)(CreateGameDialogContainer)
