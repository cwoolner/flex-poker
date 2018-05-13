import React from 'react';
import WebSocketSubscriptionManager from '../webSocket/WebSocketSubscriptionManager';
import { HashRouter } from 'react-router-dom';
import { Route, Switch } from 'react-router';
import GameTabs from './GameTabs';
import Lobby from '../lobby/Lobby';
import GamePage from '../game/GamePage';
import TablePage from '../table/TablePage';
import Logout from './Logout';
import Chat from './Chat';

export default (props) => {
  return (
    <HashRouter>
      <div>
        <GameTabs />
        <Switch>
          <Route exact path="/" component={Lobby} />
          <Route exact path="/game/:gameId" component={GamePage} />
          <Route exact path="/game/:gameId/table/:tableId" component={TablePage} />
          <Route exact path="/logout" component={Logout} />
        </Switch>
        <Chat />
      </div>
    </HashRouter>
  )
}
