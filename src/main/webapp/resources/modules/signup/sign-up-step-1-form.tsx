import { Button, Form } from "react-bootstrap"

export default ({ error, submitFormCallback }) => {
  return (
    <Form className="standard-form" id="sign-up-step-1-form" onSubmit={submitFormCallback}>
      <h3 className="mb-3 fw-normal">Sign Up</h3>
      { error ? <p className="error-message">{error}</p> : null }
      <Form.FloatingLabel label="Username" className="mb-1">
        <Form.Control type="text" name="username" placeholder="Username" required autoFocus />
      </Form.FloatingLabel>
      <Form.FloatingLabel label="Password" className="mb-1">
        <Form.Control type="password" name="password" placeholder="Password" required />
      </Form.FloatingLabel>
      <Form.FloatingLabel label="Email" className="mb-1">
        <Form.Control type="email" name="emailAddress" placeholder="Email" required />
      </Form.FloatingLabel>
      <Button type="submit" variant="primary" className="mt-3 w-100 btn-lg">Sign Up</Button>
    </Form>
  )
}
