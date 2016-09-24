import React from 'react';
import { Button, Table } from 'react-bootstrap';

export default ({gameList, gameOpenedCallback, openCreateGameModalCallback}) => {
  return (
    <div className={'game-list'}>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Name</th>
            <th>Stage</th>
            <th>Registered Players</th>
            <th>Total Players</th>
            <th>Players Per Table</th>
            <th>Creator</th>
            <th>Created</th>
            <th style={{textAlign: 'center'}}><Button bsStyle="primary" onClick={openCreateGameModalCallback}><span className="glyphicon glyphicon-plus-sign"></span></Button></th>
          </tr>
        </thead>
        <tbody>
          {
            gameList.length === 0
              ? <tr><td colSpan="8">No games to show</td></tr>
              : gameList.map((game, index) => {
                return (
                  <tr key={index}>
                    <td>{game.name}</td>
                    <td>{game.stage}</td>
                    <td>{game.numberOfRegisteredPlayers}</td>
                    <td>{game.maxNumberOfPlayers}</td>
                    <td>{game.maxPlayersPerTable}</td>
                    <td>{game.createdBy}</td>
                    <td>{game.createdOn}</td>
                    <td style={{textAlign: 'center'}}><Button onClick={() => gameOpenedCallback(game.id)} disabled={game.stage !== 'REGISTERING'}>Open Game</Button></td>
                  </tr>
                )
              })
          }
        </tbody>
      </Table>
    </div>
  )
}
