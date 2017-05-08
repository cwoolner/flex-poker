import React from 'react';
import WebSocketService from '../webSocket/WebSocketService';
import WebSocketSubscriptionManager from '../webSocket/WebSocketSubscriptionManager';
import CommonCards from './CommonCards';
import MyCards from './MyCards';
import Seat from './Seat';
import PokerActions from './PokerActions';
import SeatContainer from './SeatContainer';
import Chat from '../common/Chat';
import _ from 'lodash';

class TablePage extends React.Component {

  constructor(props) {
    super(props)

    this.displayChat = this.displayChat.bind(this)
    this.sendChat = this.sendChat.bind(this)
    this.receiveTableUpdate = this.receiveTableUpdate.bind(this)

    this.state = {
      myLeftCardId: null,
      myRightCardId: null,
      totalPot: 0,
      visibleCommonCards: [],
      seats: [],
      tableVersion: 0,
      pots: []
    }
  }

  componentDidMount() {
    const { gameId, tableId } = this.props.match.params
    WebSocketSubscriptionManager.subscribe(this, [
      {location: `/topic/game/${gameId}/table/${tableId}`, subscription: this.receiveTableUpdate},
      {location: `/topic/chat/game/${gameId}/table/${tableId}/user`, subscription: this.displayChat},
      {location: `/topic/chat/game/${gameId}/table/${tableId}/system`, subscription: this.displayChat}
    ]);

    document.addEventListener(`pocketCardsReceived-${tableId}`, evt => {
      this.setState({
        myLeftCardId: evt.detail.cardId1,
        myRightCardId: evt.detail.cardId2
      })
    });

  }

  componentWillUnmount() {
    WebSocketSubscriptionManager.unsubscribe(this);
  }

  displayChat(message) {
    this.refs.tableChat.displayChat(message.body);
  }

  sendChat(message) {
    const { gameId, tableId } = this.props.match.params
    const tableMessage = {
      message,
      receiverUsernames: null,
      gameId,
      tableId
    };

    WebSocketService.send('/app/sendchatmessage', tableMessage);
  }

  receiveTableUpdate(message) {
    let table = JSON.parse(message.body);

    if (table.version > this.state.tableVersion) {
      this.setState({
        totalPot: table.totalPot,
        visibleCommonCards: table.visibleCommonCards,
        seats: table.seats,
        tableVersion: table.version,
        pots: table.pots
      });
    }
  }

  render() {
    const { gameId, tableId } = this.props.match.params
    const username = window.username;
    const mySeat = this.state.seats.find(seat => seat.name === username);

    return (
      <div>
        <div className={"poker-table"}>
          <div>{this.state.totalPot}</div>
          <CommonCards visibleCommonCards={this.state.visibleCommonCards} />
          <SeatContainer gameId={gameId} tableId={tableId} mySeat={mySeat} seats={this.state.seats} />
        </div>

        {this.state.pots.map(pot => {
          return(
            <div>
              <div>{pot.seats}</div>
              <div>{pot.amount}</div>
              <div>{pot.winners}</div>
              <div>{pot.open ? 'open' : 'closed'}</div>
            </div>
          )
        })}

        <MyCards myLeftCardId={this.state.myLeftCardId} myRightCardId={this.state.myRightCardId} />
        {
          _.isNil(mySeat)
            ? null
            : <PokerActions
                gameId={gameId}
                tableId={tableId}
                actionOn={mySeat.actionOn}
                callAmount={mySeat.callAmount}
                minRaiseTo={mySeat.raiseTo}
                maxRaiseTo={mySeat.chipsInBack + mySeat.chipsInFront} />
        }

        <Chat ref="tableChat" sendChat={this.sendChat} />
      </div>
    )
  }

}

export default TablePage
