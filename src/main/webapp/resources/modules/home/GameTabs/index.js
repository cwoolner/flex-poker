import React from 'react';
import { Nav, NavItem } from 'react-bootstrap';
import { connect } from 'react-redux';
import GameTab from './GameTab';

const GameTabs = ({openGameTabs}) => {
  return (
    <Nav variant="tabs">
      <NavItem href="/#">Lobby</NavItem>
      {openGameTabs.map((openGameTab, index) => <GameTab key={index} openGameTab={openGameTab} />)}
    </Nav>
  );
}

const mapStateToProps = state => ({ openGameTabs: state.openGameTabs })

export default connect(mapStateToProps)(GameTabs);
