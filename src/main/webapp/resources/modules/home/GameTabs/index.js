import React from 'react'
import Nav from 'react-bootstrap/Nav'
import { connect } from 'react-redux'
import GameTab from './GameTab'

const GameTabs = ({openGameTabs}) => {
  return (
    <Nav variant="tabs">
      <Nav.Item href="/#">Lobby</Nav.Item>
      {openGameTabs.map((openGameTab, index) => <GameTab key={index} openGameTab={openGameTab} />)}
    </Nav>
  );
}

const mapStateToProps = state => ({ openGameTabs: state.openGameTabs })

export default connect(mapStateToProps)(GameTabs);
