export default ({ username, email }) => {
  return (
    <>
      <h3 className="mb-3 fw-normal">Confirm your sign up</h3>
      <p>Email sent to {email} (TODO: not working yet)</p>
      <p><a href={`/sign-up-confirm?username=${username}`}>Click here to confirm</a></p>
    </>
  )
}
