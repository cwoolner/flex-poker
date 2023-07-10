import { Button, Form } from "react-bootstrap"

export default ({ error, signUpCode, submitFormCallback }) => {
  return (
    <Form className="standard-form" id="sign-up-step-2-form" onSubmit={submitFormCallback}>
      <h3 className="mb-3 fw-normal">Sign Up</h3>
      {
        error
          ? <p className="error-message">{error}</p>
          : null
      }
      <Form.FloatingLabel label="Username" className="mb-1">
        <Form.Control type="text" name="username" placeholder="Username" required autoFocus />
      </Form.FloatingLabel>
      <input type="hidden" name="signUpCode" value={signUpCode} />
      <Button type="submit" variant="primary" className="mt-3 w-100 btn-lg">Confirm Sign Up</Button>
    </Form>
  )
}
