import React from 'react'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'
import Navigation from './modules/home/Navigation';
import MainTabs from './modules/home/MainTabs';

export default () => {
  return (
    <div>
      <Navigation username={window.username} />
      <div className="container">
        <MainTabs />
      </div>
    </div>
  )
}
