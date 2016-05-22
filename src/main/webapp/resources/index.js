import React from 'react'
import { render } from 'react-dom'
import { Router, Route, browserHistory, IndexRoute } from 'react-router'
import Home from './modules/home/Home'
import handleRoutes from './router';
import webSocketService from './modules/common/webSocketService';

webSocketService.registerSubscription('/user/topic/chat/personal/user', message => alert('personal' + message.body));
webSocketService.registerSubscription('/user/topic/chat/personal/system', message => alert('personal' + message.body));

window.onhashchange = handleRoutes;

window.dispatchEvent(new Event('hashchange'));

render((
  <Router history={browserHistory}>
    <Route path="/" component={Home}>

    </Route>
  </Router>
), document.getElementById('app'))
