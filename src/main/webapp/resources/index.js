import React from 'react';
import { render } from 'react-dom';
import { HashRouter } from 'react-router-dom';
import WebSocketSubscriptionManager from './modules/webSocket/WebSocketSubscriptionManager';
import Navigation from './modules/home/Navigation';
import MainTabs from './modules/home/MainTabs';

WebSocketSubscriptionManager.subscribe(this, [
  {location: '/user/topic/chat/personal/user', subscription: message => alert('personal' + message.body)},
  {location: '/user/topic/chat/personal/system', subscription: message => alert('personal' + message.body)}
]);

render((
  <div>
    <Navigation username={window.username} />
    <div className="container">
      <HashRouter>
        <MainTabs />
      </HashRouter>
    </div>
  </div>
), document.getElementById('app'))
