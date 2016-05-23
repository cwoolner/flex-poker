import React from 'react';
import webSocketService from '../common/webSocketService';
import cardData from './cardData';
import CommonCards from './CommonCards';
import MyCards from './MyCards';

export default React.createClass({

  getInitialState() {
    return {
      myLeftCard: null,
      myRightCard: null,
      totalPot: 0,
      visibleCommonCards: []
    }
  },

  componentDidMount() {
    const gameId = this.props.params.gameId;
    const tableId = this.props.params.tableId;

    webSocketService.registerSubscription(`/topic/game/${gameId}/table/${tableId}`, receiveTableUpdate.bind(this));
    webSocketService.registerSubscription(`/topic/chat/game/${gameId}/table/${tableId}/user`, displayChat.bind(this));
    webSocketService.registerSubscription(`/topic/chat/game/${gameId}/table/${tableId}/system`, displayChat.bind(this));

    document.querySelector('.table-chat').addEventListener('chat-msg-entered', evt => {
      sendChat(evt.detail, gameId, tableId);
    });

    document.addEventListener(`pocketCardsReceived-${tableId}`, evt => {
      this.setState({
        myLeftCard: cardData[evt.detail.cardId1],
        myRightCard: cardData[evt.detail.cardId2],
        totalPot: this.state.totalPot,
        visibleCommonCards: this.state.visibleCommonCards
      })
    });

  },

  render() {
    return (
      <div>
        <p>{this.props.params.gameId}</p>
        <p>{this.props.params.tableId}</p>

        <div className={"poker-table"}>
          <div>{this.state.totalPot}</div>
          <CommonCards visibleCommonCards={this.state.visibleCommonCards} />
          <div class="seat-holder"></div>
        </div>

        <MyCards myLeftCard={this.state.myLeftCard} myRightCard={this.state.myRightCard} />

        <div class="poker-actions">
          <button id="check-button" onClick={evt => check(this.props.params.gameId, this.props.params.tableId)}>Check</button>
          <button id="call-button" onClick={evt => call(this.props.params.gameId, this.props.params.tableId)}>Call</button>
          <button id="raise-button" onClick={evt => raise(this.props.params.gameId, this.props.params.tableId, 0)}>Raise</button>
          <button id="fold-button" onClick={evt => fold(this.props.params.gameId, this.props.params.tableId)}>Fold</button>
          <input type="checkbox" id="check-checkbox" /><label for="check-checkbox">Check</label>
          <input type="checkbox" id="call-checkbox" /><label for="call-checkbox">Call</label>
          <input type="checkbox" id="raise-checkbox" /><label for="raise-checkbox">Raise</label>
          <input type="checkbox" id="fold-checkbox" /><label for="fold-checkbox">Fold</label>
        </div>

        <fp-chat class="table-chat"></fp-chat>
      </div>
    )
  }

})

function displayChat(message) {
  document.querySelector('fp-chat').displayChat(message.body);
}

function sendChat(message, gameId, tableId) {
  const tableMessage = {
    message: message,
    receiverUsernames: null,
    gameId: gameId,
    tableId: tableId
  };

  webSocketService.send('/app/sendchatmessage', tableMessage);
}

function check(gameId, tableId) {
  webSocketService.send('/app/check', {gameId: gameId, tableId: tableId});
}

function call(gameId, tableId) {
  webSocketService.send('/app/call', {gameId: gameId, tableId: tableId});
}

function raise(gameId, tableId, raiseToAmount) {
  webSocketService.send('/app/raise', {gameId: gameId, tableId: tableId, raiseToAmount: raiseToAmount});
}

function fold(gameId, tableId) {
  webSocketService.send('/app/fold', {gameId: gameId, tableId: tableId});
}

function receiveTableUpdate(message) {
  let table = JSON.parse(message.body);

  this.setState({
    myLeftCard: this.state.myLeftCard,
    myRightCard: this.state.myRightCard,
    totalPot: table.totalPot,
    visibleCommonCards: table.visibleCommonCards
  });

/*
  let username = document.querySelector('.username').innerHTML;

  let mySeat = table.seats.find(element => element.name === username);
  let seatHolder = this.shadowRoot.querySelector('.seat-holder');

  if (!seatHolder.hasChildNodes()) {
    table.seats.forEach(seat => {
      let seatElement = document.createElement('fp-seat');
      seatElement.dataset.position = seat.position;
      seatHolder.appendChild(seatElement);
    });
  }

  table.seats.forEach(seat => {
    let seatElement = [].slice.call(seatHolder.children)
      .find(element => element.dataset.position === seat.position.toString());
    seatElement.populateSeatInfo(seat);
    seatElement.toggleMySeat(seat === mySeat);
  });

  let pokerActionsArea = this.shadowRoot.querySelector('.poker-actions');

  [].slice.call(pokerActionsArea.children).forEach(
    pokerActionElement => pokerActionElement.className += ' display-none ');

  if (mySeat) {
    var pokerActions = {
      actionOn: mySeat.actionOn,
      check: mySeat.callAmount === 0,
      fold: mySeat.callAmount !== 0,
      call: mySeat.callAmount !== 0,
      raise: mySeat.raiseTo !== 0
    };

    if (pokerActions.actionOn) {
      if (pokerActions.check) {
        let checkButton = this.shadowRoot.querySelector('#check-button');
        checkButton.className = checkButton.className.replace(/display-none/g, '').trim();
      }

      if (pokerActions.call) {
        let callButton = this.shadowRoot.querySelector('#call-button');
        callButton.className = callButton.className.replace(/display-none/g, '').trim();
      }

      if (pokerActions.raise) {
        let raiseButton = this.shadowRoot.querySelector('#raise-button');
        raiseButton.className = raiseButton.className.replace(/display-none/g, '').trim();
      }

      if (pokerActions.fold) {
        let foldButton = this.shadowRoot.querySelector('#fold-button');
        foldButton.className = foldButton.className.replace(/display-none/g, '').trim();
      }
    } else {
      if (pokerActions.check) {
        let checkCheckbox = this.shadowRoot.querySelector('#check-checkbox');
        checkCheckbox.className = checkCheckbox.className.replace(/display-none/g, '').trim();
        checkCheckbox.nextSibling.className = checkCheckbox.nextSibling.className.replace(/display-none/g, '').trim();
      }

      if (pokerActions.call) {
        let callCheckbox = this.shadowRoot.querySelector('#call-checkbox');
        callCheckbox.className = callCheckbox.className.replace(/display-none/g, '').trim();
        callCheckbox.nextSibling.className = callCheckbox.nextSibling.className.replace(/display-none/g, '').trim();
      }

      if (pokerActions.raise) {
        let raiseCheckbox = this.shadowRoot.querySelector('#raise-checkbox');
        raiseCheckbox.className = raiseCheckbox.className.replace(/display-none/g, '').trim();
        raiseCheckbox.nextSibling.className = raiseCheckbox.nextSibling.className.replace(/display-none/g, '').trim();
      }

      if (pokerActions.fold) {
        let foldCheckbox = this.shadowRoot.querySelector('#fold-checkbox');
        foldCheckbox.className = foldCheckbox.className.replace(/display-none/g, '').trim();
        foldCheckbox.nextSibling.className = foldCheckbox.nextSibling.className.replace(/display-none/g, '').trim();
      }
    }

  }
*/
}
