import React from 'react';
import WebSocketService from '../webSocket/WebSocketService';
import { Modal, Button, FormControl, FormGroup, ControlLabel } from 'react-bootstrap';
import { hashHistory } from 'react-router';

export default React.createClass({

  hideDialog() {
    this.props.hideDialog();
  },

  joinGameFormSubmitted(evt) {
    evt.preventDefault();
    WebSocketService.send('/app/joingame', this.props.gameId);
    this.hideDialog();
    hashHistory.push(`/game/${this.props.gameId}`);
  },

  render() {
    return (
      <Modal show={this.props.showModal} onHide={this.hideDialog} {...this.props}>
        <Modal.Header>
          <button className={"close"} onClick={this.hideDialog}>X</button>
          <Modal.Title>Join Game</Modal.Title>
        </Modal.Header>
        <form id="join-game-form" onSubmit={this.joinGameFormSubmitted}>
          <Modal.Body>
            <FormGroup>
              <ControlLabel>Current Balance</ControlLabel>
              <FormControl.Static>100</FormControl.Static>
            </FormGroup>
            <FormGroup>
              <ControlLabel>Cost</ControlLabel>
              <FormControl.Static>10</FormControl.Static>
            </FormGroup>
            <FormGroup>
              <ControlLabel>Remaining Balance</ControlLabel>
              <FormControl.Static>90</FormControl.Static>
            </FormGroup>
          </Modal.Body>
          <Modal.Footer>
            <Button onClick={this.hideDialog}>Close</Button>
            <Button type="submit" bsStyle="primary" autoFocus>Join Game</Button>
          </Modal.Footer>
        </form>
      </Modal>
    )
  }
})
