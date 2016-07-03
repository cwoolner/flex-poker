import React from 'react';
import WebSocketService from '../webSocket/WebSocketService';

export default ({gameId, tableId, actionOn, callAmount, raiseTo}) => {
  return (
    <div>
      <div className={actionOn ? '' : 'hidden'}>
        <button className={callAmount === 0 ? '' : 'hidden'} onClick={evt => check(gameId, tableId)}>Check</button>
        <button className={callAmount > 0 ? '' : 'hidden'} onClick={evt => call(gameId, tableId)}>Call</button>
        <button className={callAmount > 0 ? '' : 'hidden'} onClick={evt => raise(gameId, tableId, 0)}>Raise</button>
        <button className={raiseTo > 0 ? '' : 'hidden'} onClick={evt => fold(gameId, tableId)}>Fold</button>
      </div>
      <div className={actionOn ? 'hidden' : ''}>
        <input type="checkbox" id="check-checkbox" /><label for="check-checkbox">Check</label>
        <input type="checkbox" id="call-checkbox" /><label for="call-checkbox">Call</label>
        <input type="checkbox" id="raise-checkbox" /><label for="raise-checkbox">Raise</label>
        <input type="checkbox" id="fold-checkbox" /><label for="fold-checkbox">Fold</label>
      </div>
    </div>
  )
}

function check(gameId, tableId) {
  WebSocketService.send('/app/check', { gameId, tableId });
}

function call(gameId, tableId) {
  WebSocketService.send('/app/call', { gameId, tableId });
}

function raise(gameId, tableId, raiseToAmount) {
  WebSocketService.send('/app/raise', { gameId, tableId, raiseToAmount });
}

function fold(gameId, tableId) {
  WebSocketService.send('/app/fold', { gameId, tableId });
}
