import { Button, Form } from "react-bootstrap"

export default () => {
    return (
      <>
        <Form className="standard-form" action="/login" method="post">
          <h3 className="mb-3 fw-normal">Log in to Flex Poker</h3>
          { window.location.search.includes('error') ? <p className="error-message">Invalid username/password</p> : null }
          <Form.FloatingLabel label="Username" className="mb-1">
            <Form.Control type="text" name="username" placeholder="Username" required autoFocus />
          </Form.FloatingLabel>
          <Form.FloatingLabel label="Password" className="mb-1">
            <Form.Control type="password" name="password" placeholder="Password" required />
          </Form.FloatingLabel>
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
          <Button type="submit" variant="primary" className="mt-3 w-100 btn-lg">Log In</Button>
        </Form>

        <div className="row row-top-buffer">
          <div className="text-center">
            Don't have an account? <a href="/sign-up">Sign Up!</a>
          </div>
        </div>
      </>
    )
}
