import React from 'react'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'
import Container from 'react-bootstrap/Container'
import Navigation from './modules/home/Navigation'
import MainTabs from './modules/home/MainTabs'

export default () => {
  return (
    <>
      <Navigation username={window.username} />
      <Container>
        <MainTabs />
      </Container>
    </>
  )
}
