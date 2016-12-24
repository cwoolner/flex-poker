import React from 'react';
import WebSocketSubscriptionManager from '../webSocket/WebSocketSubscriptionManager';
import { Redirect, Route, Switch } from 'react-router';
import { Nav, NavItem, NavDropdown, MenuItem } from 'react-bootstrap';
import _ from 'lodash';
import GameTabs from './GameTabs';
import Lobby from '../lobby/Lobby';
import GamePage from '../game/GamePage';
import TablePage from '../table/TablePage';
import Logout from './Logout';
import { INIT_OPEN_GAME_TABS, UPDATE_OPEN_GAME_TABS, UPDATE_OPEN_GAME_LIST } from '../../constants/ActionTypes';

class MainTabs extends React.Component {

  constructor(props) {
    super(props)

    this.openGameTab = this.openGameTab.bind(this)
    this.openTable = this.openTable.bind(this)
    this.displayGameTabs = this.displayGameTabs.bind(this)
    this.displayPocketCards = this.displayPocketCards.bind(this)
    this.updateGameList = this.updateGameList.bind(this)

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
      {location: '/user/queue/pocketcards', subscription: this.displayPocketCards},
      {location: '/topic/availabletournaments', subscription: this.updateGameList}
    ]);
  }

  componentWillUnmount() {
    WebSocketSubscriptionManager.unsubscribe(this);
  }

  displayGameTabs(message) {
    this.props.store.dispatch({type: INIT_OPEN_GAME_TABS, openGameTabs: JSON.parse(message.body)});
    this.setState({
      tableToRedirectTo: null,
      gameToRedirectTo: null
    });
  }

  openGameTab(message) {
    const currentOpenGameTabs = this.props.store.getState().openGameTabs;
    const newOpenGameTabs = JSON.parse(message.body);
    const gameToRedirectTo = newOpenGameTabs.filter(x => !(currentOpenGameTabs.map(y => y.gameId).includes(x.gameId)));

    this.props.store.dispatch({type: UPDATE_OPEN_GAME_TABS, openGameTabs: newOpenGameTabs});
    this.setState({
      tableToRedirectTo: null,
      gameToRedirectTo: gameToRedirectTo.length === 0 ? null : `/game/${gameToRedirectTo[0].gameId}`
    });
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

  updateGameList(message) {
    this.props.store.dispatch({type: UPDATE_OPEN_GAME_LIST, openGameList: JSON.parse(message.body)});
  }

  render() {
    return (
      <div>
        <GameTabs openGameTabs={this.props.openGameTabs} />
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
