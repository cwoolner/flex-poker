import React from 'react'
import Container from 'react-bootstrap/Container'
import Navbar from 'react-bootstrap/Navbar'
import Nav from 'react-bootstrap/Nav'
import NavDropdown from 'react-bootstrap/NavDropdown'

export default ({username}) => {
  return (
    <Navbar bg="light">
      <Container>
        <Navbar.Brand href="#/">Flex Poker</Navbar.Brand>
        <Navbar.Collapse className="justify-content-end">
          <Nav>
            <NavDropdown title={username} id="collasible-nav-dropdown">
              <NavDropdown.Item eventKey="3.1">Profile</NavDropdown.Item>
              <NavDropdown.Item eventKey="3.2">Account</NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item eventKey="3.3" href="#/logout">Logout</NavDropdown.Item>
            </NavDropdown>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}
