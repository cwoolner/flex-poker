import { Nav } from 'react-bootstrap'
import { useSelector } from 'react-redux'
import GameTab from './GameTab'
import { LinkContainer } from 'react-router-bootstrap'
import { RootState } from '../../..'

export default () => {
  const openGameTabs = useSelector((state: RootState) => state.openGameTabs)

  return (
    <Nav variant="tabs">
      <Nav.Item>
        <LinkContainer to="/">
          <Nav.Link>Lobby</Nav.Link>
        </LinkContainer>
      </Nav.Item>
      {openGameTabs.map((openGameTab, index) => <GameTab key={index} openGameTab={openGameTab} />)}
    </Nav>
  )
}
