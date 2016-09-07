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
    if (_.inRange(raiseToAmount, this.props.minRaiseTo, this.props.maxRaiseTo + 1)) {
      WebSocketService.send('/app/raise', { gameId, tableId, raiseToAmount });
      this.setState({
        currentRaiseTo: null
      });
    }
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

  componentWillReceiveProps(nextProps) {
    this.state.currentRaiseTo = nextProps.minRaiseTo;
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
          <button className={callAmount === 0 ? '' : 'hidden'} onClick={evt => this.check(gameId, tableId)}>Check</button>
          <button className={callAmount > 0 ? '' : 'hidden'} onClick={evt => this.call(gameId, tableId)}>Call {callAmount}</button>
          <button className={minRaiseTo > 0 ? '' : 'hidden'} onClick={evt => this.raise(gameId, tableId, this.state.currentRaiseTo)}>
            Raise to {_.inRange(this.state.currentRaiseTo, minRaiseTo, maxRaiseTo + 1) ? this.state.currentRaiseTo : '--'}
          </button>
          <input type="number" min={minRaiseTo} max={maxRaiseTo} value={this.state.currentRaiseTo} onChange={this.onRaiseToChange} />
          <label className={_.inRange(this.state.currentRaiseTo, minRaiseTo, maxRaiseTo + 1) ? 'hidden' : ''}>Invalid raise</label>
          <button className={callAmount > 0 ? '' : 'hidden'} onClick={evt => this.fold(gameId, tableId)}>Fold</button>
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
