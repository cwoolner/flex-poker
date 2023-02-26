import React from 'react'
import Button from 'react-bootstrap/Button'
import Table from 'react-bootstrap/Table'
import moment from 'moment-timezone'

export default ({gameList, gameOpenedCallback, openCreateGameModalCallback}) => {
  return (
    <div className={'game-list'}>
      <Table striped bordered hover size="sm">
        <thead>
          <tr>
            <th>Name</th>
            <th>Stage</th>
            <th>Registered Players</th>
            <th>Total Players</th>
            <th>Players Per Table</th>
            <th>Creator</th>
            <th>Created</th>
            <th style={{textAlign: 'center'}}>
              <Button variant="primary" onClick={openCreateGameModalCallback}>+</Button>
            </th>
          </tr>
        </thead>
        <tbody>
          {
            gameList.length === 0
              ? <tr><td colSpan={10}>No games to show</td></tr>
              : gameList.map((game, index) => {
                return (
                  <tr key={index}>
                    <td>{game.name}</td>
                    <td>{game.stage}</td>
                    <td>{game.numberOfRegisteredPlayers}</td>
                    <td>{game.maxNumberOfPlayers}</td>
                    <td>{game.maxPlayersPerTable}</td>
                    <td>{game.createdBy}</td>
                    <td>{moment.parseZone(game.createdOn)
                               .tz(moment.tz.guess())
                               .format("YYYY-MM-DD h:mm:ss a")}</td>
                    <td style={{textAlign: 'center'}}>
                      <Button onClick={() => gameOpenedCallback(game.id)} disabled={game.stage !== 'REGISTERING'}>Open</Button>
                    </td>
                  </tr>
                )
              })
          }
        </tbody>
      </Table>
    </div>
  )
}
