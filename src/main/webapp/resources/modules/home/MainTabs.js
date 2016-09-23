import React from 'react';
import WebSocketSubscriptionManager from '../webSocket/WebSocketSubscriptionManager';
import { Redirect, Link, Match, Miss } from 'react-router';
import { Nav, NavItem, NavDropdown, MenuItem } from 'react-bootstrap';
import _ from 'lodash';
import GameTab from './GameTab';
import Lobby from '../lobby/Lobby';
import GamePage from '../game/GamePage';
import TablePage from '../table/TablePage';
import Logout from './Logout';

export default React.createClass({

  getInitialState() {
    return {
      openGameTabs: [],
      tableToRedirectTo: null,
      gameToRedirectTo: null
    }
  },

  componentDidMount() {
    WebSocketSubscriptionManager.subscribe(this, [
      {location: '/user/queue/errors', subscription: message => alert("Error " + message.body)},
      {location: '/app/opengamesforuser', subscription: displayGameTabs.bind(this)},
      {location: '/user/queue/opengamesforuser', subscription: openGameTab.bind(this)},
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
        <Match exactly pattern="/" component={Lobby} />
        <Match exactly pattern="/game/:gameId" component={GamePage} />
        <Match exactly pattern="/game/:gameId/table/:tableId" component={TablePage} />
        <Match exactly pattern="/logout" component={Logout} />
        {_.isNil(this.state.tableToRedirectTo) ? null : <Redirect to={this.state.tableToRedirectTo || ""} />}
        {_.isNil(this.state.gameToRedirectTo) ? null : <Redirect to={this.state.gameToRedirectTo || ""} />}
      </div>
    )
  }
})

function displayGameTabs(message) {
  this.setState({
    openGameTabs: JSON.parse(message.body),
    tableToRedirectTo: null,
    gameToRedirectTo: null
  });
}

function openGameTab(message) {
  const newOpenGameTabs = JSON.parse(message.body);
  const gameToRedirectTo = newOpenGameTabs.filter(x => !(this.state.openGameTabs.map(y => y.gameId).includes(x.gameId)));

  this.setState({
    openGameTabs: newOpenGameTabs,
    tableToRedirectTo: null,
    gameToRedirectTo: gameToRedirectTo.length === 0 ? null : `/game/${gameToRedirectTo[0].gameId}`
  });
}

function openTable(message) {
  const openTable = JSON.parse(message.body);
  this.setState({
    tableToRedirectTo: `/game/${openTable.gameId}/table/${openTable.tableId}`,
    gameToRedirectTo: null
  });
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
