import React from 'react'
import { Nav, Navbar, NavItem, NavDropdown } from 'react-bootstrap'
import Dropdown from 'react-bootstrap/Dropdown'

export default ({username}) => {
  return (
    <Navbar>
      <Navbar.Header>
        <Navbar.Brand>
          <a href="#/">Flex Poker</a>
        </Navbar.Brand>
      </Navbar.Header>
      <Nav pullRight>
        <NavDropdown eventKey={3} title={username} id="user-actions-dropdown">
          <Dropdown.Item eventKey={3.1}>Profile</Dropdown.Item>
          <Dropdown.Item eventKey={3.2}>Account</Dropdown.Item>
          <Dropdown.Item divider />
          <Dropdown.Item eventKey={3.3} href="#/logout">Logout</Dropdown.Item>
        </NavDropdown>
      </Nav>
    </Navbar>
  );
}
