import React from 'react';
import { Nav, Navbar, NavItem, NavDropdown, MenuItem } from 'react-bootstrap';
import { connect } from 'react-redux';

const Navigation = ({username}) => {
  return (
    <Navbar>
      <Navbar.Header>
        <Navbar.Brand>
          <a href="#/">Flex Poker</a>
        </Navbar.Brand>
      </Navbar.Header>
      <Nav pullRight>
        <NavDropdown eventKey={3} title={username} id="user-actions-dropdown">
          <MenuItem eventKey={3.1}>Profile</MenuItem>
          <MenuItem eventKey={3.2}>Account</MenuItem>
          <MenuItem divider />
          <MenuItem eventKey={3.3} href="#/logout">Logout</MenuItem>
        </NavDropdown>
      </Nav>
    </Navbar>
  );
}

const mapStateToProps = state => ({ username: state.username })

export default connect(mapStateToProps)(Navigation)
