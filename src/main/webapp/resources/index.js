import React from 'react'
import { render } from 'react-dom'
import { Router, Route, hashHistory, IndexRoute } from 'react-router'
import Home from './modules/home/Home'
import MainTabs from './modules/home/MainTabs'
import WebSocketSubscriptionManager from './modules/webSocket/WebSocketSubscriptionManager';
import GamePage from './modules/game/GamePage';
import TablePage from './modules/table/TablePage';
import Logout from './modules/home/Logout';

const subscriptions = [];
subscriptions.push({location: '/user/topic/chat/personal/user', subscription: message => alert('personal' + message.body)});
subscriptions.push({location: '/user/topic/chat/personal/system', subscription: message => alert('personal' + message.body)});

WebSocketSubscriptionManager.subscribe(this, subscriptions);

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
