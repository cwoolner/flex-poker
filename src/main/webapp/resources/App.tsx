import { Container } from 'react-bootstrap'
import Navigation from './modules/home/Navigation'
import MainTabs from './modules/home/MainTabs'
import { useDispatch, useSelector, useStore } from 'react-redux'
import { RootState } from '.'
import staticSubscriptions from './modules/home/staticSubscriptions'
import chatMessageSubscriber from './modules/home/Chat/chatMessageSubscriber'
import tableStateSubscriber from './modules/table/tableStateSubscriber'
import WebSocketService from './modules/webSocket/WebSocketService'
import Login from './modules/login'
import { Route, Routes } from 'react-router-dom'
import { useEffect } from 'react'
import { loginUser, logoutUser } from './reducers'

export default () => {
  const { username, loggedIn } = useSelector((state: RootState) => state.userInfo)
  const store = useStore()
  const dispatch = useDispatch()

  useEffect(() => {
    fetch('/userinfo')
      .then(x => x.json())
      .then(data => {
        if (data.loggedIn) {
          WebSocketService.connect()
          staticSubscriptions(store.dispatch)
          store.subscribe(chatMessageSubscriber(store.dispatch)(store))
          store.subscribe(tableStateSubscriber(store.dispatch)(store))
          dispatch(loginUser(data.username))
        } else {
          dispatch(logoutUser())
        }
      })
  }, [])

  return (
    <>
      <Navigation username={username} />
      <Container>
        {
          loggedIn
            ? <MainTabs />
            : <Routes>
                <Route path="/login" element={<Login />} />
              </Routes>
        }
      </Container>
    </>
  )
}
