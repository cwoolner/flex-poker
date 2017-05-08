import React from 'react';
import Seat from './Seat';
import WebSocketSubscriptionManager from '../webSocket/WebSocketSubscriptionManager';

class SeatContainer extends React.Component {

  constructor(props) {
    super(props)
    this.receiveActionOnTick = this.receiveActionOnTick.bind(this)
    this.state = {
      actionOnTick: 0
    }
  }

  componentDidMount() {
    const gameId = this.props.gameId;
    const tableId = this.props.tableId;

    WebSocketSubscriptionManager.subscribe(this, [
      {location: `/topic/game/${gameId}/table/${tableId}/action-on-tick`, subscription: this.receiveActionOnTick}
    ]);
  }

  componentWillUnmount() {
    WebSocketSubscriptionManager.unsubscribe(this);
  }

  receiveActionOnTick(message) {
    this.setState({
      actionOnTick: message.body
    });
  }

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

}

export default SeatContainer
