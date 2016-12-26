import React from 'react';
import { render } from 'react-dom';
import { createStore } from 'redux';
import { Provider } from 'react-redux';
import WebSocketSubscriptionManager from './modules/webSocket/WebSocketSubscriptionManager';
import reducer from './reducers';
import App from './App';

WebSocketSubscriptionManager.subscribe(this, [
  {location: '/user/topic/chat/personal/user', subscription: message => alert('personal' + message.body)},
  {location: '/user/topic/chat/personal/system', subscription: message => alert('personal' + message.body)}
]);

const store = createStore(reducer);

render((
  <Provider store={store}>
    <App />
  </Provider>
), document.getElementById('app'))
