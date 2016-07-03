import React from 'react'
import { render } from 'react-dom'
import { Router, Route, hashHistory, IndexRoute } from 'react-router'
import Home from './modules/home/Home'
import MainTabs from './modules/home/MainTabs'
import WebSocketService from './modules/webSocket/WebSocketService';
import GamePage from './modules/game/GamePage';
import TablePage from './modules/table/TablePage';
import Logout from './modules/home/Logout';

WebSocketService.registerSubscription('/user/topic/chat/personal/user', message => alert('personal' + message.body));
WebSocketService.registerSubscription('/user/topic/chat/personal/system', message => alert('personal' + message.body));

render((
  <Router history={hashHistory}>
    <Route path="/" component={MainTabs}>
      <IndexRoute component={Home} />
      <Route path="/game/:gameId" component={GamePage} />
      <Route path="/game/:gameId/table/:tableId" component={TablePage} />
      <Route path="/logout" component={Logout} />
    </Route>
  </Router>
), document.getElementById('app'))
