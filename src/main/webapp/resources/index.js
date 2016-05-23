import React from 'react'
import { render } from 'react-dom'
import { Router, Route, hashHistory, IndexRoute } from 'react-router'
import Home from './modules/home/Home'
import handleRoutes from './router';
import webSocketService from './modules/common/webSocketService';
import GamePage from './modules/game/GamePage';

webSocketService.registerSubscription('/user/topic/chat/personal/user', message => alert('personal' + message.body));
webSocketService.registerSubscription('/user/topic/chat/personal/system', message => alert('personal' + message.body));

render((
  <Router history={hashHistory}>
    <Route path="/" component={Home} />
    <Route path="/game/:gameId" component={GamePage} />
  </Router>
), document.getElementById('app'))
