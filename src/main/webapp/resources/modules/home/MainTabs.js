import React from 'react';
import { connect } from 'react-redux'
import _ from 'lodash'
import { HashRouter } from 'react-router-dom'
import { Redirect, Route, Switch } from 'react-router'
import GameTabs from './GameTabs'
import Lobby from '../lobby/Lobby'
import GamePage from '../game/GamePage'
import TablePage from '../table/TablePage'
import Logout from './Logout'
import Chat from './Chat'

const MainTabs = ({ redirectUrl }) => {
  return (
    <HashRouter>
      <div>
        <GameTabs />
        <Switch>
          <Route exact path="/" component={Lobby} />
          <Route exact path="/game/:gameId" component={GamePage} />
          <Route exact path="/game/:gameId/table/:tableId" component={TablePage} />
          <Route exact path="/logout" component={Logout} />
        </Switch>
        {_.isNil(redirectUrl) ? null : <Redirect to={redirectUrl} />}
        <Chat />
      </div>
    </HashRouter>
  )
}

const mapStateToProps = state => ({ redirectUrl: state.redirectUrl })

export default connect(mapStateToProps)(MainTabs)
