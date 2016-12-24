import React from 'react';
import { render } from 'react-dom';
import { HashRouter } from 'react-router-dom';
import { createStore } from 'redux';
import { Provider } from 'react-redux';
import WebSocketSubscriptionManager from './modules/webSocket/WebSocketSubscriptionManager';
import Navigation from './modules/home/Navigation';
import MainTabs from './modules/home/MainTabs';
import reducer from './reducers';
import { UPDATE_USERNAME } from './constants/ActionTypes';

WebSocketSubscriptionManager.subscribe(this, [
  {location: '/user/topic/chat/personal/user', subscription: message => alert('personal' + message.body)},
  {location: '/user/topic/chat/personal/system', subscription: message => alert('personal' + message.body)}
]);

const store = createStore(reducer);
store.dispatch({type: UPDATE_USERNAME, username: window.username});

render((
  <Provider store={store}>
    <div>
      <Navigation />
      <div className="container">
        <HashRouter>
          <MainTabs store={store} />
        </HashRouter>
      </div>
    </div>
  </Provider>
), document.getElementById('app'))
