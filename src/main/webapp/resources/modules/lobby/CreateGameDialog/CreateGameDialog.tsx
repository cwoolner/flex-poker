import { Button, Modal, Form } from 'react-bootstrap'

export default ({showModal, hideDialogCallback, submitFormCallback}) => {
 return (
    <Modal size="sm" show={showModal} onHide={hideDialogCallback}>
      <Modal.Header closeButton>
        <Modal.Title>Create Game</Modal.Title>
      </Modal.Header>
      <form id="create-game-form" onSubmit={submitFormCallback}>
        <Modal.Body>
          <Form.Group>
            <Form.Label>Name</Form.Label>
            <Form.Control type="text" name="name" autoFocus />
          </Form.Group>
          <Form.Group>
            <Form.Label>Number of Players (2 - 90)</Form.Label>
            <Form.Control type="number" name="players" min="2" max="90" />
          </Form.Group>
          <Form.Group>
            <Form.Label>Number of Players per Table (2 - 9)</Form.Label>
            <Form.Control type="number" name="playersPerTable" min="2" max="9" />
          </Form.Group>
          <Form.Group>
            <Form.Label>Blind increment in minutes (1 - 60)</Form.Label>
            <Form.Control type="number" name="numberOfMinutesBetweenBlindLevels" min="1" max="60" />
          </Form.Group>
          <Form.Group>
            <Form.Label>Blind timer in seconds (1 - 60)</Form.Label>
            <Form.Control type="number" name="secondsForActionOnTimer" min="1" max="60" />
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={hideDialogCallback}>Close</Button>
          <Button type="submit" variant="primary">Create Game</Button>
        </Modal.Footer>
      </form>
    </Modal>
  )
}
