import React from 'react';
import WebSocketService from '../webSocket/WebSocketService';
import _ from 'lodash';

export default React.createClass({

  getInitialState() {
    return {
      currentRaiseTo: null
    };
  },

  componentWillReceiveProps({minRaiseTo}) {
    this.setState({
      currentRaiseTo: minRaiseTo
    });
  },

  componentDidMount() {
    if (_.isNil(this.state.currentRaiseTo)) {
      this.setState({
        currentRaiseTo: this.props.minRaiseTo
      });
    }
  },

  render() {
    const {gameId, tableId, actionOn, callAmount, minRaiseTo, maxRaiseTo} = this.props;

    return (
      <div>
        <div className={actionOn ? '' : 'hidden'}>
          <button className={callAmount === 0 ? '' : 'hidden'} onClick={check.bind(this, gameId, tableId)}>Check</button>
          <button className={callAmount > 0 ? '' : 'hidden'} onClick={call.bind(this, gameId, tableId)}>Call {callAmount}</button>
          <button className={minRaiseTo > 0 ? '' : 'hidden'} onClick={raise.bind(this, gameId, tableId, this.state.currentRaiseTo, minRaiseTo, maxRaiseTo)}>
            Raise to {validRaise(this.state.currentRaiseTo, minRaiseTo, maxRaiseTo) ? this.state.currentRaiseTo : '--'}
          </button>
          <input type="number" min={minRaiseTo} max={maxRaiseTo} value={this.state.currentRaiseTo} onChange={evt => this.setState({ currentRaiseTo: evt.target.value })} />
          <label className={_.inRange(this.state.currentRaiseTo, minRaiseTo, maxRaiseTo + 1) ? 'hidden' : ''}>Invalid raise</label>
          <button className={callAmount > 0 ? '' : 'hidden'} onClick={fold.bind(this, gameId, tableId)}>Fold</button>
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

});

function check(gameId, tableId) {
  WebSocketService.send('/app/check', { gameId, tableId });
  this.setState({
    currentRaiseTo: null
  });
}

function call(gameId, tableId) {
  WebSocketService.send('/app/call', { gameId, tableId });
  this.setState({
    currentRaiseTo: null
  });
}

function raise(gameId, tableId, raiseToAmount, minRaiseTo, maxRaiseTo) {
  if (validRaise(raiseToAmount, minRaiseTo, maxRaiseTo)) {
    WebSocketService.send('/app/raise', { gameId, tableId, raiseToAmount });
    this.setState({
      currentRaiseTo: null
    });
  }
}

function fold(gameId, tableId) {
  WebSocketService.send('/app/fold', { gameId, tableId });
  this.setState({
    currentRaiseTo: null
  });
}

function validRaise(raiseToAmount, minRaiseTo, maxRaiseTo) {
  return _.inRange(raiseToAmount, minRaiseTo, maxRaiseTo + 1);
}
