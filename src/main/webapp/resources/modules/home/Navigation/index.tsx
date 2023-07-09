import { Container, Navbar, Nav, NavDropdown } from 'react-bootstrap'
import Logout from '../Logout'

export default ({username}) => {
  return (
    <Navbar bg="light">
      <Container>
        <Navbar.Brand href="/">Flex Poker</Navbar.Brand>
        {
          username
          ? <Navbar.Collapse className="justify-content-end">
              <Nav>
                <NavDropdown title={username} id="collasible-nav-dropdown">
                  <NavDropdown.Item eventKey="3.1">Profile</NavDropdown.Item>
                  <NavDropdown.Item eventKey="3.2">Account</NavDropdown.Item>
                  <NavDropdown.Divider />
                  <NavDropdown.Item eventKey="3.3" onClick={() => Logout()}>Logout</NavDropdown.Item>
                </NavDropdown>
              </Nav>
            </Navbar.Collapse>
          : null
        }
      </Container>
    </Navbar>
  )
}
