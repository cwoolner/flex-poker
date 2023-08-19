import { useEffect, useState } from 'react';
import WebSocketService from '../webSocket/WebSocketService'

const check = (gameId, tableId, setCurrentRaiseTo) => {
  WebSocketService.send('/app/check', { gameId, tableId })
  setCurrentRaiseTo(null)
}

const call = (gameId, tableId, setCurrentRaiseTo) => {
  WebSocketService.send('/app/call', { gameId, tableId })
  setCurrentRaiseTo(null)
}

const fold = (gameId, tableId, setCurrentRaiseTo) => {
  WebSocketService.send('/app/fold', { gameId, tableId })
  setCurrentRaiseTo(null)
}

const validRaise = (minRaiseTo, maxRaiseTo, currentRaiseTo) => {
  return currentRaiseTo >= minRaiseTo && currentRaiseTo <= maxRaiseTo
}

const raise = (gameId, tableId, minRaiseTo, maxRaiseTo, currentRaiseTo, setCurrentRaiseTo) => {
  if (validRaise(minRaiseTo, maxRaiseTo, currentRaiseTo)) {
    WebSocketService.send('/app/raise', { gameId, tableId, raiseToAmount: currentRaiseTo })
    setCurrentRaiseTo(null)
  }
}

const handleRaiseChangeEvent = (setCurrentRaiseTo, evt) => {
  setCurrentRaiseTo(evt.target.value)
}

const PokerActions = ({ gameId, tableId, actionOn, callAmount, minRaiseTo, maxRaiseTo }) => {

  const [currentRaiseTo, setCurrentRaiseTo] = useState(null)

  useEffect(() => {
    setCurrentRaiseTo(minRaiseTo)
  })

  return (
    <div>
      <div className={actionOn ? '' : 'hidden'}>
        <button className={callAmount === 0 ? '' : 'hidden'} onClick={check.bind(null, gameId, tableId, setCurrentRaiseTo)}>Check</button>
        <button className={callAmount > 0 ? '' : 'hidden'} onClick={call.bind(null, gameId, tableId, setCurrentRaiseTo)}>Call {callAmount}</button>
        <button className={minRaiseTo > 0 ? '' : 'hidden'} onClick={raise.bind(null, gameId, tableId, minRaiseTo, maxRaiseTo, currentRaiseTo, setCurrentRaiseTo)}>
          Raise to {validRaise(minRaiseTo, maxRaiseTo, currentRaiseTo) ? currentRaiseTo : '--'}
        </button>
        <input type="number" min={minRaiseTo} max={maxRaiseTo} value={currentRaiseTo || ""}
               onChange={handleRaiseChangeEvent.bind(null, setCurrentRaiseTo)} />
        <label className={validRaise(minRaiseTo, maxRaiseTo, currentRaiseTo) ? 'hidden' : ''}>Invalid raise</label>
        <button className={callAmount > 0 ? '' : 'hidden'} onClick={fold.bind(null, gameId, tableId, setCurrentRaiseTo)}>Fold</button>
      </div>
      <div className={actionOn ? 'hidden' : ''}>
        <input type="checkbox" id="check-checkbox" /><label htmlFor="check-checkbox">Check</label>
        <input type="checkbox" id="call-checkbox" /><label htmlFor="call-checkbox">Call</label>
        <input type="checkbox" id="raise-checkbox" /><label htmlFor="raise-checkbox">Raise</label>
        <input type="checkbox" id="fold-checkbox" /><label htmlFor="fold-checkbox">Fold</label>
      </div>
    </div>
  )
}

export default PokerActions
