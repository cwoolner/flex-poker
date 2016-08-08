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
    const subscriptions = [];
    subscriptions.push({location: '/user/queue/errors', subscription: message => alert("Error " + message.body)});
    subscriptions.push({location: '/app/opengamesforuser', subscription: message => displayGameTabs.call(this, message)});
    subscriptions.push({location: '/user/queue/opengamesforuser', subscription: message => displayGameTabs.call(this, message)});
    subscriptions.push({location: '/user/queue/opentable', subscription: message => {
      const openTable = JSON.parse(message.body);
      hashHistory.push(`/game/${openTable.gameId}/table/${openTable.tableId}`);
    }});
    subscriptions.push({location: '/user/queue/personaltablestatus', subscription: message => alert(JSON.parse(message.body))});
    subscriptions.push({location: '/user/queue/pocketcards', subscription: message => {
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
    }});
    WebSocketSubscriptionManager.subscribe(this, subscriptions);
  },

  componentWillUnmount() {
    WebSocketSubscriptionManager.unsubscribe(this);
  },

  render() {
    return (
      <div>
        <Nav bsStyle="tabs">
          <NavItem href="/">Lobby</NavItem>
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
