import React from 'react';
import Seat from './Seat';
import WebSocketSubscriptionManager from '../webSocket/WebSocketSubscriptionManager';

export default React.createClass({

  getInitialState() {
    return {
      actionOnTick: 0
    };
  },

  componentDidMount() {
    const gameId = this.props.gameId;
    const tableId = this.props.tableId;

    const subscriptions = [];
    subscriptions.push({location: `/topic/game/${gameId}/table/${tableId}/action-on-tick`, subscription: receiveActionOnTick.bind(this)});
    WebSocketSubscriptionManager.subscribe(this, subscriptions);
  },

  componentWillUnmount() {
    WebSocketSubscriptionManager.unsubscribe(this);
  },

  render() {
    return (
      <div className={"seat-holder"}>
        {
          this.props.seats.map((seat, index) =>
            <Seat seat={seat} mySeat={seat === this.props.mySeat} key={index} actionOnTick={this.state.actionOnTick} />
          )
        }
      </div>
    )
  }
})

function receiveActionOnTick(message) {
  this.setState({
    actionOnTick: message.body
  });
}
