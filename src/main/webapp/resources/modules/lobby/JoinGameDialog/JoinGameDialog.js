import React from 'react'
import { Modal, Button, FormControl, FormGroup } from 'react-bootstrap'
import Form from 'react-bootstrap/Form'

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
            <Form.Label>Current Balance</Form.Label>
            <FormControl.Static>100</FormControl.Static>
          </FormGroup>
          <FormGroup>
            <Form.Label>Cost</Form.Label>
            <FormControl.Static>10</FormControl.Static>
          </FormGroup>
          <FormGroup>
            <Form.Label>Remaining Balance</Form.Label>
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
