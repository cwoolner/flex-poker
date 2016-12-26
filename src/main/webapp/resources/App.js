import React from 'react'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'
import { HashRouter, Match, Miss } from 'react-router';
import Navigation from './modules/home/Navigation';
import MainTabs from './modules/home/MainTabs';
import * as actions from './actions'

const App = ({actions}) => {
  return (
    <div>
      <Navigation username={window.username} />
      <div className="container">
        <HashRouter>
          <MainTabs actions={actions} />
        </HashRouter>
      </div>
    </div>
  )
}

const mapStateToProps = state => ({})

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(actions, dispatch)
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(App)
