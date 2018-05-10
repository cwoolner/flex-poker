import React from 'react';
import { render } from 'react-dom';
import { createStore } from 'redux';
import { Provider } from 'react-redux';
import WebSocketSubscriptionManager from './modules/webSocket/WebSocketSubscriptionManager';
import reducer from './reducers';
import App from './App';
import chatMessageSubscriber from './modules/home/Chat/chatMessageSubscriber'

const store = createStore(reducer);

store.subscribe(chatMessageSubscriber(store))

render((
  <Provider store={store}>
    <App />
  </Provider>
), document.getElementById('app'))
