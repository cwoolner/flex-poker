import React from 'react';
import webSocketService from '../common/webSocketService';

export default React.createClass({

  hideDialog() {
    this.props.hideDialog();
  },

  potentiallyHideDialog(evt) {
    if (evt.keyCode === 27) {
      this.hideDialog();
    }
  },

  joinGameFormSubmitted(evt) {
    evt.preventDefault();
    webSocketService.send('/app/joingame', this.props.gameId);
    this.hideDialog();
  },

  render() {
    return (
      <div {...this.props}>
        <div className="overlay" onClick={this.hideDialog}></div>
        <div className="content">
          <div className="close" onClick={this.hideDialog}>X</div>
          <form id="join-game-form" onKeyDown={this.potentiallyHideDialog} onSubmit={this.joinGameFormSubmitted}>
            <div>
              <label>Current balance:</label> <span id="current-balance">100</span>
            </div>
            <div>
              <label>Cost:</label> <span id="cost-of-game">10</span>
            </div>
            <div>
              <label>Remaining balance:</label> <span id="remaining-balance">90</span>
            </div>
            <input type="submit" value="Join" />
          </form>
        </div>
      </div>
    )
  }
})
