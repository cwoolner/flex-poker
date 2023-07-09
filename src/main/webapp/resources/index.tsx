import { createRoot } from 'react-dom/client'
import { createStore } from 'redux'
import { Provider } from 'react-redux'
import reducer from './reducers'
import App from './App'
import { BrowserRouter } from 'react-router-dom'

const store = createStore(reducer)

export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch

const container = document.getElementById('app')
const root = createRoot(container)

root.render((
  <Provider store={store}>
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </Provider>
))
