import { NavDropdown, Dropdown, DropdownDivider } from 'react-bootstrap'
import { LinkContainer } from 'react-router-bootstrap'

const DropdownLink = ({ link, text }) =>
  <LinkContainer to={link}>
    <Dropdown.Item>{text}</Dropdown.Item>
  </LinkContainer>

const MyTableItem = ({ openGameTab }) =>
  openGameTab.myTableId
    ? <DropdownLink link={`/game/${openGameTab.gameId}/table/${openGameTab.myTableId}`} text={'My Table'} />
    : null

const GamePageItem = ({ openGameTab }) =>
  <DropdownLink link={`/game/${openGameTab.gameId}`} text={'Game Page'} />

const OtherTableItems = ({ openGameTab }) => {
  return (
    <>
      {openGameTab.viewingTables.length > 0 ? <Dropdown.Divider /> : null}
      {openGameTab.viewingTables.map(viewingTable =>
        <DropdownLink link={`/game/${openGameTab.gameId}/table/${viewingTable}`} text={viewingTable} />)}
    </>
)}

export default ({openGameTab}) => {
  return (
    <NavDropdown title={openGameTab.name}>
      <MyTableItem openGameTab={openGameTab} />
      <GamePageItem openGameTab={openGameTab} />
      <OtherTableItems openGameTab={openGameTab} />
    </NavDropdown>
  )
}
