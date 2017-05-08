import React from 'react';
import WebSocketSubscriptionManager from '../webSocket/WebSocketSubscriptionManager';
import { Redirect, Route, Switch } from 'react-router';
import { Nav, NavItem, NavDropdown, MenuItem } from 'react-bootstrap';
import _ from 'lodash';
import GameTab from './GameTab';
import Lobby from '../lobby/Lobby';
import GamePage from '../game/GamePage';
import TablePage from '../table/TablePage';
import Logout from './Logout';

class MainTabs extends React.Component {

  constructor(props) {
    super(props)

    this.openGameTab = this.openGameTab.bind(this)
    this.openTable = this.openTable.bind(this)
    this.displayGameTabs = this.displayGameTabs.bind(this)
    this.displayPocketCards = this.displayPocketCards.bind(this)

    this.state = {
      openGameTabs: [],
      tableToRedirectTo: null,
      gameToRedirectTo: null
    }
  }

  componentDidMount() {
    WebSocketSubscriptionManager.subscribe(this, [
      {location: '/user/queue/errors', subscription: message => alert("Error " + message.body)},
      {location: '/app/opengamesforuser', subscription: this.displayGameTabs},
      {location: '/user/queue/opengamesforuser', subscription: this.openGameTab},
      {location: '/user/queue/opentable', subscription: this.openTable},
      {location: '/user/queue/personaltablestatus', subscription: message => alert(JSON.parse(message.body))},
      {location: '/user/queue/pocketcards', subscription: this.displayPocketCards}
    ]);
  }

  componentWillUnmount() {
    WebSocketSubscriptionManager.unsubscribe(this);
  }

  displayGameTabs(message) {
    this.setState({
      openGameTabs: JSON.parse(message.body),
      tableToRedirectTo: null,
      gameToRedirectTo: null
    })
  }

  openGameTab(message) {
    const newOpenGameTabs = JSON.parse(message.body)
    const gameToRedirectTo = newOpenGameTabs.filter(x => !(this.state.openGameTabs.map(y => y.gameId).includes(x.gameId)))

    this.setState({
      openGameTabs: newOpenGameTabs,
      tableToRedirectTo: null,
      gameToRedirectTo: gameToRedirectTo.length === 0 ? null : `/game/${gameToRedirectTo[0].gameId}`
    })
}

  openTable(message) {
    const openTable = JSON.parse(message.body)
    this.setState({
      tableToRedirectTo: `/game/${openTable.gameId}/table/${openTable.tableId}`,
      gameToRedirectTo: null
    })
  }

  displayPocketCards(message) {
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

  render() {
    return (
      <div>
        <Nav bsStyle="tabs">
          <NavItem href="/#">Lobby</NavItem>
          {this.state.openGameTabs.map((openGameTab, index) => <GameTab key={index} openGameTab={openGameTab} />)}
        </Nav>
        <Switch>
          <Route exact path="/" component={Lobby} />
          <Route exact path="/game/:gameId" component={GamePage} />
          <Route exact path="/game/:gameId/table/:tableId" component={TablePage} />
          <Route exact path="/logout" component={Logout} />
        </Switch>
        {_.isNil(this.state.tableToRedirectTo) ? null : <Redirect to={this.state.tableToRedirectTo || ""} />}
        {_.isNil(this.state.gameToRedirectTo) ? null : <Redirect to={this.state.gameToRedirectTo || ""} />}
      </div>
    )
  }

}

export default MainTabs
