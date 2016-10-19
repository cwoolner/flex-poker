import React from 'react';
import { Nav, NavItem } from 'react-bootstrap';
import GameTab from './GameTab';

export default ({openGameTabs}) => {
  return (
    <Nav bsStyle="tabs">
      <NavItem href="/#">Lobby</NavItem>
      {openGameTabs.map((openGameTab, index) => <GameTab key={index} openGameTab={openGameTab} />)}
    </Nav>
  );
}
