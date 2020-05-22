import React from 'react'
import Nav from 'react-bootstrap/Nav'
import { useSelector } from 'react-redux'
import GameTab from './GameTab'

export default () => {
  const openGameTabs = useSelector(state => state.openGameTabs)

  return (
    <Nav variant="tabs">
      <Nav.Item>
        <Nav.Link href="/#">Lobby</Nav.Link>
      </Nav.Item>
      {openGameTabs.map((openGameTab, index) => <GameTab key={index} openGameTab={openGameTab} />)}
    </Nav>
  )
}
