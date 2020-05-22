import React from 'react'
import { render } from 'react-dom'
import { createStore } from 'redux'
import { Provider } from 'react-redux'
import reducer from './reducers'
import App from './App'
import staticSubscriptions from './modules/home/staticSubscriptions'
import chatMessageSubscriber from './modules/home/Chat/chatMessageSubscriber'
import tableStateSubscriber from './modules/table/tableStateSubscriber'

const store = createStore(reducer)

staticSubscriptions(store.dispatch)
store.subscribe(chatMessageSubscriber(store.dispatch)(store))
store.subscribe(tableStateSubscriber(store.dispatch)(store))

render((
  <Provider store={store}>
    <App />
  </Provider>
), document.getElementById('app'))
