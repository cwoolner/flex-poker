import WebSocketService from '../../webSocket/WebSocketService'
import { useSelector, useDispatch } from 'react-redux'
import { hideCreateGameModal } from '../../../reducers'
import CreateGameDialog from './CreateGameDialog'
import { RootState } from '../../..'

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

export default () => {
  const dispatch = useDispatch()
  const dispatchHideCreateGameModal = () => dispatch(hideCreateGameModal())

  const showModal = useSelector((state: RootState) => state.showCreateGameModal)

  return (
    <CreateGameDialog showModal={showModal}
                      hideDialogCallback={dispatchHideCreateGameModal}
                      submitFormCallback={createGameFormSubmitted.bind(null, dispatchHideCreateGameModal)} />
  )
}
