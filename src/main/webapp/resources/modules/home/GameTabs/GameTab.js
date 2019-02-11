import React from 'react'
import { NavDropdown } from 'react-bootstrap'
import Dropdown from 'react-bootstrap/Dropdown'

export default ({openGameTab}) => {
  return (
    <NavDropdown title={openGameTab.name}>
      {openGameTab.myTableId ? <Dropdown.Item href={`#/game/${openGameTab.gameId}/table/${openGameTab.myTableId}`}>My Table</Dropdown.Item> : null}
      <Dropdown.Item href={`#/game/${openGameTab.gameId}`}>Game Page</Dropdown.Item>
      {openGameTab.viewingTables.length > 0 ? <Dropdown.Item divider /> : null}
      {openGameTab.viewingTables.map((viewingTable, index) => <Dropdown.Item key={index} href={`#/game/${openGameTab.gameId}/table/${openGameTab.tableId}`} />)}
    </NavDropdown>
  )
}
