import React from 'react';
import { Modal, Button, FormControl, FormGroup, ControlLabel } from 'react-bootstrap';

export default ({gameId, showModal, hideDialogCallback, submitFormCallback}) => {
  return (
    <Modal bsSize="small" show={showModal} onHide={hideDialogCallback}>
      <Modal.Header>
        <button className={"close"} onClick={hideDialogCallback}>X</button>
        <Modal.Title>Join Game</Modal.Title>
      </Modal.Header>
      <form id="join-game-form" onSubmit={submitFormCallback}>
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
          <Button onClick={hideDialogCallback}>Close</Button>
          <Button type="submit" bsStyle="primary" autoFocus>Join Game</Button>
        </Modal.Footer>
      </form>
    </Modal>
  )
}
