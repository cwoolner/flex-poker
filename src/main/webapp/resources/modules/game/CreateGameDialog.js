import React from 'react';
import WebSocketService from '../webSocket/WebSocketService';
import { Modal, Button, FormControl, FormGroup, ControlLabel } from 'react-bootstrap';

export default ({showModal, hideDialog}) => {

  const createGameFormSubmitted = (evt) => {
    evt.preventDefault();
    const nameElement = evt.target.elements[0];
    const playersElement = evt.target.elements[1];
    const playersPerTableElement = evt.target.elements[2];
    const numberOfMinutesBetweenBlindLevelsElement = evt.target.elements[3];

    WebSocketService.send('/app/creategame', {
      name: nameElement.value,
      players: playersElement.value,
      playersPerTable: playersPerTableElement.value,
      numberOfMinutesBetweenBlindLevels: numberOfMinutesBetweenBlindLevelsElement.value
    });

    hideDialog();
  };

 return (
    <Modal bsSize="small" show={showModal} onHide={hideDialog}>
      <Modal.Header>
        <button className={"close"} onClick={hideDialog}>X</button>
        <Modal.Title>Create Game</Modal.Title>
      </Modal.Header>
      <form id="create-game-form" onSubmit={createGameFormSubmitted}>
        <Modal.Body>
          <FormGroup>
            <ControlLabel>Name</ControlLabel>
            <FormControl type="text" name="name" autoFocus />
          </FormGroup>
          <FormGroup>
            <ControlLabel>Number of Players (2 - 90)</ControlLabel>
            <FormControl type="number" name="players" min="2" max="90" />
          </FormGroup>
          <FormGroup>
            <ControlLabel>Number of Players per Table (2 - 9)</ControlLabel>
            <FormControl type="number" name="playersPerTable" min="2" max="9" />
          </FormGroup>
          <FormGroup>
            <ControlLabel>Blind increment in minutes (1 - 60)</ControlLabel>
            <FormControl type="number" name="numberOfMinutesBetweenBlindLevels" min="1" max="60" />
          </FormGroup>
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={hideDialog}>Close</Button>
          <Button type="submit" bsStyle="primary">Create Game</Button>
        </Modal.Footer>
      </form>
    </Modal>
  );

}
