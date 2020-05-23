import React from 'react'
import Nav from 'react-bootstrap/Nav'
import { useSelector } from 'react-redux'
import GameTab from './GameTab'
import { IndexLinkContainer } from 'react-router-bootstrap'

export default () => {
  const openGameTabs = useSelector(state => state.openGameTabs)

  return (
    <Nav variant="tabs">
      <Nav.Item>
        <IndexLinkContainer to="/">
          <Nav.Link>Lobby</Nav.Link>
        </IndexLinkContainer>
      </Nav.Item>
      {openGameTabs.map((openGameTab, index) => <GameTab key={index} openGameTab={openGameTab} />)}
    </Nav>
  )
}
