import React from 'react'
import Button from 'react-bootstrap/Button'
import Modal from 'react-bootstrap/Modal'
import Form from 'react-bootstrap/Form'

export default ({gameId, showModal, hideDialogCallback, submitFormCallback}) => {
  return (
    <Modal size="sm" show={showModal} onHide={hideDialogCallback}>
      <Modal.Header closeButton>
        <Modal.Title>Join Game</Modal.Title>
      </Modal.Header>
      <form id="join-game-form" onSubmit={submitFormCallback}>
        <Modal.Body>
          <Form.Group>
            <Form.Label>Current Balance</Form.Label>
            <Form.Control readOnly defaultValue="100" />
          </Form.Group>
          <Form.Group>
            <Form.Label>Cost</Form.Label>
            <Form.Control readOnly defaultValue="10" />
          </Form.Group>
          <Form.Group>
            <Form.Label>Remaining Balance</Form.Label>
            <Form.Control readOnly defaultValue="90" />
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={hideDialogCallback}>Close</Button>
          <Button type="submit" variant="primary" autoFocus>Join Game</Button>
        </Modal.Footer>
      </form>
    </Modal>
  )
}
