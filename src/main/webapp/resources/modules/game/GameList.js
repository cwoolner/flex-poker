import React from 'react';

export default React.createClass({

  openGame(gameId) {
    this.props.gameOpenedCallback(gameId);
  },

  render() {
    return (
      <div {...this.props}>
        <table className={'table table-striped table-bordered table-hover'}>
          <thead>
            <tr>
              <th>Name</th>
              <th>Stage</th>
              <th>Registered Players</th>
              <th>Total Players</th>
              <th>Players Per Table</th>
              <th>Creator</th>
              <th>Created</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {
              this.props.gameList.map((game, index) => {
                return (
                  <tr key={index}>
                    <td>{game.name}</td>
                    <td>{game.stage}</td>
                    <td>{game.numberOfRegisteredPlayers}</td>
                    <td>{game.maxNumberOfPlayers}</td>
                    <td>{game.maxPlayersPerTable}</td>
                    <td>{game.createdBy}</td>
                    <td>{game.createdOn}</td>
                    <td><button className={'btn'} onClick={this.openGame.bind(this, game.id)}>Open Game</button></td>
                  </tr>
                )
              })
            }
          </tbody>
        </table>
      </div>
    )
  }
})
