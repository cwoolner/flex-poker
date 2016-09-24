import React from 'react'
import { render } from 'react-dom'
import WebSocketSubscriptionManager from './modules/webSocket/WebSocketSubscriptionManager';
import Navigation from './modules/common/Navigation';
import Router from './modules/common/Router';

WebSocketSubscriptionManager.subscribe(this, [
  {location: '/user/topic/chat/personal/user', subscription: message => alert('personal' + message.body)},
  {location: '/user/topic/chat/personal/system', subscription: message => alert('personal' + message.body)}
]);

render((
  <div>
    <Navigation username={window.username} />
    <div className="container">
      <Router />
    </div>
  </div>
), document.getElementById('app'))
