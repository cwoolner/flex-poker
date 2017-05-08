import React from 'react';
import WebSocketService from '../webSocket/WebSocketService';
import _ from 'lodash';

class PokerActions extends React.Component {

  constructor(props) {
    super(props)

    this.check = this.check.bind(this)
    this.call = this.call.bind(this)
    this.raise = this.raise.bind(this)
    this.fold = this.fold.bind(this)
    this.validRaise = this.validRaise.bind(this)
    this.handleRaiseChangeEvent = this.handleRaiseChangeEvent.bind(this)

    this.state = {
      currentRaiseTo: null
    }
  }

  componentWillReceiveProps({minRaiseTo}) {
    this.setState({
      currentRaiseTo: minRaiseTo
    });
  }

  componentDidMount() {
    if (_.isNil(this.state.currentRaiseTo)) {
      this.setState({
        currentRaiseTo: this.props.minRaiseTo
      });
    }
  }

  check() {
    const { gameId, tableId } = this.props
    WebSocketService.send('/app/check', { gameId, tableId });
    this.setState({
      currentRaiseTo: null
    });
  }

  call() {
    const { gameId, tableId } = this.props
    WebSocketService.send('/app/call', { gameId, tableId });
    this.setState({
      currentRaiseTo: null
    });
  }

  raise() {
    const { gameId, tableId } = this.props
    const raiseToAmount = this.state.currentRaiseTo
    if (this.validRaise()) {
      WebSocketService.send('/app/raise', { gameId, tableId, raiseToAmount });
      this.setState({
        currentRaiseTo: null
      });
    }
  }

  fold() {
    const { gameId, tableId } = this.props
    WebSocketService.send('/app/fold', { gameId, tableId });
    this.setState({
      currentRaiseTo: null
    });
  }

  validRaise() {
    const { minRaiseTo, maxRaiseTo } = this.props
    return _.inRange(this.state.currentRaiseTo, minRaiseTo, maxRaiseTo + 1);
  }

  handleRaiseChangeEvent(evt) {
    this.setState({
      currentRaiseTo: evt.target.value
    });
  }

  render() {
    const { actionOn, callAmount, minRaiseTo, maxRaiseTo} = this.props;

    return (
      <div>
        <div className={actionOn ? '' : 'hidden'}>
          <button className={callAmount === 0 ? '' : 'hidden'} onClick={this.check}>Check</button>
          <button className={callAmount > 0 ? '' : 'hidden'} onClick={this.call}>Call {callAmount}</button>
          <button className={minRaiseTo > 0 ? '' : 'hidden'} onClick={this.raise}>
            Raise to {this.validRaise() ? this.state.currentRaiseTo : '--'}
          </button>
          <input type="number" min={minRaiseTo} max={maxRaiseTo} value={this.state.currentRaiseTo} onChange={this.handleRaiseChangeEvent} />
          <label className={this.validRaise() ? 'hidden' : ''}>Invalid raise</label>
          <button className={callAmount > 0 ? '' : 'hidden'} onClick={this.fold}>Fold</button>
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

}

export default PokerActions
