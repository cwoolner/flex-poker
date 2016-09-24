import React from 'react';
import WebSocketSubscriptionManager from '../webSocket/WebSocketSubscriptionManager';
import { hashHistory, Link } from 'react-router';
import { Nav, NavItem, NavDropdown, MenuItem } from 'react-bootstrap';
import GameTab from './GameTab';

export default React.createClass({

  getInitialState() {
    return {
      openGameTabs: []
    }
  },

  componentDidMount() {
    WebSocketSubscriptionManager.subscribe(this, [
      {location: '/user/queue/errors', subscription: message => alert("Error " + message.body)},
      {location: '/app/opengamesforuser', subscription: displayGameTabs.bind(this)},
      {location: '/user/queue/opengamesforuser', subscription: displayGameTabs.bind(this)},
      {location: '/user/queue/opentable', subscription: openTable.bind(this)},
      {location: '/user/queue/personaltablestatus', subscription: message => alert(JSON.parse(message.body))},
      {location: '/user/queue/pocketcards', subscription: displayPocketCards.bind(this)}
    ]);
  },

  componentWillUnmount() {
    WebSocketSubscriptionManager.unsubscribe(this);
  },

  render() {
    return (
      <div>
        <Nav bsStyle="tabs">
          <NavItem href="/#">Lobby</NavItem>
         {this.state.openGameTabs.map((openGameTab, index) => <GameTab key={index} openGameTab={openGameTab} />)}
        </Nav>
        {this.props.children}
      </div>
    )
  }
})

function displayGameTabs(message) {
  this.setState({
    openGameTabs: JSON.parse(message.body)
  });
}

function openTable(message) {
  const openTable = JSON.parse(message.body);
  hashHistory.push(`/game/${openTable.gameId}/table/${openTable.tableId}`);
}

function displayPocketCards(message) {
  const parsedData = JSON.parse(message.body);
  const pocketCards = {
    cardId1: parsedData.cardId1,
    cardId2: parsedData.cardId2
  };

  const pocketCardsReceivedEvent = new CustomEvent(`pocketCardsReceived-${parsedData.tableId}`, {
    detail: pocketCards,
    bubbles: true
  });
  document.dispatchEvent(pocketCardsReceivedEvent);
}
