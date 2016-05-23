import React from 'react';
import webSocketService from '../common/webSocketService';
import cardData from './cardData';
import CommonCards from './CommonCards';
import MyCards from './MyCards';
import Seat from './Seat';
import PokerActions from './PokerActions';
import Chat from '../common/Chat';
import _ from 'lodash';

export default React.createClass({

  getInitialState() {
    return {
      myLeftCard: null,
      myRightCard: null,
      totalPot: 0,
      visibleCommonCards: [],
      seats: []
    }
  },

  componentDidMount() {
    const gameId = this.props.params.gameId;
    const tableId = this.props.params.tableId;

    webSocketService.registerSubscription(`/topic/game/${gameId}/table/${tableId}`, receiveTableUpdate.bind(this));
    webSocketService.registerSubscription(`/topic/chat/game/${gameId}/table/${tableId}/user`, displayChat.bind(this));
    webSocketService.registerSubscription(`/topic/chat/game/${gameId}/table/${tableId}/system`, displayChat.bind(this));

    document.addEventListener(`pocketCardsReceived-${tableId}`, evt => {
      this.setState({
        myLeftCard: cardData[evt.detail.cardId1],
        myRightCard: cardData[evt.detail.cardId2],
        totalPot: this.state.totalPot,
        visibleCommonCards: this.state.visibleCommonCards,
        seats: this.state.seats
      })
    });

  },

  render() {
    const username = document.querySelector('.username').innerHTML;
    const mySeat = this.state.seats.find(seat => seat.name === username);

    return (
      <div>
        <p>{this.props.params.gameId}</p>
        <p>{this.props.params.tableId}</p>

        <div className={"poker-table"}>
          <div>{this.state.totalPot}</div>
          <CommonCards visibleCommonCards={this.state.visibleCommonCards} />
          <div className={"seat-holder"}>
            {
              this.state.seats.map((seat, index) =>
                <Seat seat={seat} mySeat={seat === mySeat} key={index} />
              )
            }
          </div>
        </div>

        <MyCards myLeftCard={this.state.myLeftCard} myRightCard={this.state.myRightCard} />
        {
          _.isNil(mySeat)
            ? null
            : <PokerActions
                gameId={this.props.params.gameId}
                tableId={this.props.params.tableId}
                actionOn={mySeat.actionOn}
                callAmount={mySeat.callAmount}
                raiseTo={mySeat.raiseTo} />
        }

        <Chat ref="tableChat" sendChat={sendChat.bind(this, this.props.params.gameId, this.props.params.tableId)} />
      </div>
    )
  }

})

function displayChat(message) {
  this.refs.tableChat.displayChat(message.body);
}

function sendChat(gameId, tableId, message) {
  const tableMessage = {
    message: message,
    receiverUsernames: null,
    gameId: gameId,
    tableId: tableId
  };

  webSocketService.send('/app/sendchatmessage', tableMessage);
}

function receiveTableUpdate(message) {
  let table = JSON.parse(message.body);

  this.setState({
    myLeftCard: this.state.myLeftCard,
    myRightCard: this.state.myRightCard,
    totalPot: table.totalPot,
    visibleCommonCards: table.visibleCommonCards,
    seats: table.seats
  });

}
