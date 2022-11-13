import React from 'react'
import _ from 'lodash'
import { BrowserRouter } from 'react-router-dom'
import { Route, Routes } from 'react-router'
import GameTabs from './GameTabs'
import Lobby from '../lobby/Lobby'
import GamePage from '../game/GamePage'
import TablePage from '../table/TablePage'
import Chat from './Chat'
import InterceptorRedirect from './InterceptRedirect'

const wrapRedirect = (component) => {
  return <InterceptorRedirect>{component}</InterceptorRedirect>
}

export default () => {
  return (
    <BrowserRouter>
      <div>
        <GameTabs />
        <Routes>
          <Route exact path="/" element={wrapRedirect(<Lobby />)} />
          <Route exact path="/game/:gameId" element={wrapRedirect(<GamePage />)} />
          <Route exact path="/game/:gameId/table/:tableId" element={wrapRedirect(<TablePage />)} />
        </Routes>
        <Chat />
      </div>
    </BrowserRouter>
  )
}
