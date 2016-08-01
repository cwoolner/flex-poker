import React from 'react';
import { Router, Route, hashHistory, IndexRoute } from 'react-router'
import Home from '../home/Home'
import MainTabs from '../home/MainTabs'
import GamePage from '../game/GamePage';
import TablePage from '../table/TablePage';
import Logout from '../home/Logout';

export default () => {
  return (
    <Router history={hashHistory}>
      <Route path="/" component={MainTabs}>
        <IndexRoute component={Home} />
        <Route path="/game/:gameId" component={GamePage} />
        <Route path="/game/:gameId/table/:tableId" component={TablePage} />
        <Route path="/logout" component={Logout} />
      </Route>
    </Router>
  );
}
