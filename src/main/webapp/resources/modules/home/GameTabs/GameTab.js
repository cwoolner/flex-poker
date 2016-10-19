import React from 'react';
import { NavDropdown, MenuItem } from 'react-bootstrap';

export default ({openGameTab}) => {
  return (
    <NavDropdown title={openGameTab.name}>
      {openGameTab.myTableId ? <MenuItem href={`#/game/${openGameTab.gameId}/table/${openGameTab.myTableId}`}>My Table</MenuItem> : null}
      <MenuItem href={`#/game/${openGameTab.gameId}`}>Game Page</MenuItem>
      {openGameTab.viewingTables.length > 0 ? <MenuItem divider /> : null}
      {openGameTab.viewingTables.map((viewingTable, index) => <MenuItem key={index} href={`#/game/${openGameTab.gameId}/table/${openGameTab.tableId}`} />)}
    </NavDropdown>
  )
}
