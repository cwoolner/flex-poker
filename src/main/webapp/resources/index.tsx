import { createRoot } from 'react-dom/client'
import { createStore } from 'redux'
import { Provider } from 'react-redux'
import reducer from './reducers'
import App from './App'
import staticSubscriptions from './modules/home/staticSubscriptions'
import chatMessageSubscriber from './modules/home/Chat/chatMessageSubscriber'
import tableStateSubscriber from './modules/table/tableStateSubscriber'

const store = createStore(reducer)

export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch

staticSubscriptions(store.dispatch)
store.subscribe(chatMessageSubscriber(store.dispatch)(store))
store.subscribe(tableStateSubscriber(store.dispatch)(store))

const container = document.getElementById('app')
const root = createRoot(container)

root.render((
  <Provider store={store}>
    <App />
  </Provider>
))
