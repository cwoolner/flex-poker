import React from 'react';
import WebSocketService from '../webSocket/WebSocketService';
import _ from 'lodash';

export default React.createClass({

  getInitialState() {
    return {
      currentRaiseTo: null
    };
  },

  check(gameId, tableId) {
    WebSocketService.send('/app/check', { gameId, tableId });
    this.setState({
      currentRaiseTo: null
    });
  },

  call(gameId, tableId) {
    WebSocketService.send('/app/call', { gameId, tableId });
    this.setState({
      currentRaiseTo: null
    });
  },

  raise(gameId, tableId, raiseToAmount) {
    WebSocketService.send('/app/raise', { gameId, tableId, raiseToAmount });
    this.setState({
      currentRaiseTo: null
    });
  },

  fold(gameId, tableId) {
    WebSocketService.send('/app/fold', { gameId, tableId });
    this.setState({
      currentRaiseTo: null
    });
  },

  onRaiseToChange(evt) {
    this.setState({
      currentRaiseTo: evt.target.value
    });
  },

  render() {
    const {gameId, tableId, actionOn, callAmount, minRaiseTo, maxRaiseTo} = this.props;

    console.log(`current: ${this.state.currentRaiseTo}`);
    console.log(`min: ${minRaiseTo}`);

    if (_.isNil(this.state.currentRaiseTo) || minRaiseTo > this.state.currentRaiseTo) {
      this.setState({
        currentRaiseTo: minRaiseTo
      });
    }

    return (
      <div>
        <div className={actionOn ? '' : 'hidden'}>
          <button className={callAmount === 0 ? '' : 'hidden'} onClick={evt => this.check(gameId, tableId)}>Check</button>
          <button className={callAmount > 0 ? '' : 'hidden'} onClick={evt => this.call(gameId, tableId)}>Call</button>
          <button className={minRaiseTo > 0 ? '' : 'hidden'} onClick={evt => this.raise(gameId, tableId, this.state.currentRaiseTo)}>Raise to {this.state.currentRaiseTo}</button>
          <label>Min: {minRaiseTo}</label>
          <input type="text" onBlur={this.onRaiseToChange} />
          <label>Max: {maxRaiseTo}</label>
          <button className={minRaiseTo > 0 ? '' : 'hidden'} onClick={evt => this.fold(gameId, tableId)}>Fold</button>
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
