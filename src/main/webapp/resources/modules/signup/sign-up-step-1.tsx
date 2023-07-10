import { useState } from "react"
import SignUpStep1Form from "./sign-up-step-1-form"
import SignUpStep1Success from "./sign-up-step-1-success"

type View = 'form' | 'success'

const submitFormCallback = (setView: (x: View) => {}, setEmail, setUsername, setError, evt) => {
  evt.preventDefault()

  const data = {
    username: evt.target.elements.username.value,
    password: evt.target.elements.password.value,
    emailAddress: evt.target.elements.emailAddress.value,
  }

  const myInit: RequestInit = {
    method: 'POST',
    cache: 'no-cache',
    redirect: 'follow',
    credentials: 'same-origin',
    headers: {
        "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  }

  fetch('/sign-up', myInit)
    .then(resp => resp.json())
    .then(data => {
      if (data.error) {
        setError(data.error)
      } else {
        setEmail(data.email)
        setUsername(data.username)
        setView('success')
      }
    })
}


export default () => {
  const [view, setView] = useState('form')
  const [email, setEmail] = useState(null)
  const [username, setUsername] = useState(null)
  const [error, setError] = useState(null)

  switch (view as View) {
    case 'form':
        return <SignUpStep1Form error={error} submitFormCallback={submitFormCallback.bind(null, setView, setEmail, setUsername, setError)} />
    case 'success':
        return <SignUpStep1Success email={email} username={username} />
  }
}
