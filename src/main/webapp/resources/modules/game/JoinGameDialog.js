import React from 'react';
import WebSocketService from '../webSocket/WebSocketService';
import { Modal, Button, FormControl, FormGroup, ControlLabel } from 'react-bootstrap';
import { hashHistory } from 'react-router';

export default ({gameId, showModal, hideDialog}) => {

  const joinGameFormSubmitted = (evt) => {
    evt.preventDefault();
    WebSocketService.send('/app/joingame', gameId);
    hideDialog();
    hashHistory.push(`/game/${gameId}`);
  };

  return (
    <Modal show={showModal} onHide={hideDialog}>
      <Modal.Header>
        <button className={"close"} onClick={hideDialog}>X</button>
        <Modal.Title>Join Game</Modal.Title>
      </Modal.Header>
      <form id="join-game-form" onSubmit={joinGameFormSubmitted}>
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
          <Button onClick={hideDialog}>Close</Button>
          <Button type="submit" bsStyle="primary" autoFocus>Join Game</Button>
        </Modal.Footer>
      </form>
    </Modal>
  );
}
