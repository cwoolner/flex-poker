import React from 'react'
import Navbar from 'react-bootstrap/Navbar'
import Nav from 'react-bootstrap/Nav'
import NavDropdown from 'react-bootstrap/NavDropdown'

export default ({username}) => {
  return (
    <Navbar>
      <Navbar.Brand>
        <a href="#/">Flex Poker</a>
      </Navbar.Brand>
      <Nav>
        <NavDropdown title={username} id="user-actions-dropdown">
          <NavDropdown.Item eventKey="3.1">Profile</NavDropdown.Item>
          <NavDropdown.Item eventKey="3.2">Account</NavDropdown.Item>
          <NavDropdown.Divider />
          <NavDropdown.Item eventKey="3.3" href="#/logout">Logout</NavDropdown.Item>
        </NavDropdown>
      </Nav>
    </Navbar>
  );
}
