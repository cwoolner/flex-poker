import React from 'react'
import { Modal, Button, FormControl, FormGroup } from 'react-bootstrap'
import Form from 'react-bootstrap/Form'

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
            <Form.Label>Name</Form.Label>
            <FormControl type="text" name="name" autoFocus />
          </FormGroup>
          <FormGroup>
            <Form.Label>Number of Players (2 - 90)</Form.Label>
            <FormControl type="number" name="players" min="2" max="90" />
          </FormGroup>
          <FormGroup>
            <Form.Label>Number of Players per Table (2 - 9)</Form.Label>
            <FormControl type="number" name="playersPerTable" min="2" max="9" />
          </FormGroup>
          <FormGroup>
            <Form.Label>Blind increment in minutes (1 - 60)</Form.Label>
            <FormControl type="number" name="numberOfMinutesBetweenBlindLevels" min="1" max="60" />
          </FormGroup>
          <FormGroup>
            <Form.Label>Blind timer in seconds (1 - 60)</Form.Label>
            <FormControl type="number" name="secondsForActionOnTimer" min="1" max="60" />
          </FormGroup>
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={hideDialogCallback}>Close</Button>
          <Button type="submit" variant="primary">Create Game</Button>
        </Modal.Footer>
      </form>
    </Modal>
  )
}
