import React from 'react';
import { Modal, Button, FormControl, FormGroup, ControlLabel } from 'react-bootstrap';

export default ({showModal, hideDialogCallback, submitFormCallback}) => {
 return (
    <Modal bsSize="small" show={showModal} onHide={hideDialogCallback}>
      <Modal.Header>
        <button className={"close"} onClick={hideDialogCallback}>X</button>
        <Modal.Title>Create Game</Modal.Title>
      </Modal.Header>
      <form id="create-game-form" onSubmit={submitFormCallback}>
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
          <FormGroup>
            <ControlLabel>Blind timer in seconds (1 - 60)</ControlLabel>
            <FormControl type="number" name="secondsForActionOnTimer" min="1" max="60" />
          </FormGroup>
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={hideDialogCallback}>Close</Button>
          <Button type="submit" bsStyle="primary">Create Game</Button>
        </Modal.Footer>
      </form>
    </Modal>
  )
}
