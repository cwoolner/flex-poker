import { useEffect, useState } from "react"
import SignUpStep2Form from "./sign-up-step-2-form"
import SignUpStep2Success from "./sign-up-step-2-success"
import { useSearchParams } from "react-router-dom"

type View = 'none' | 'form' | 'success'

const submitFormCallback = (setView: (x: View) => {}, setError, evt) => {
  evt.preventDefault()

  const data = {
    username: evt.target.elements.username.value,
    signUpCode: evt.target.elements.signUpCode.value,
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

  fetch('/sign-up-confirm', myInit)
    .then(resp => resp.json())
    .then(data => {
      if (data.error) {
        setError(data.error)
      } else {
        setView('success')
      }
    })
}

export default () => {
  const [view, setView] = useState('none')
  const [signUpCode, setSignUpCode] = useState(null)
  const [error, setError] = useState(null)
  const [searchParams, _] = useSearchParams()
  const username = searchParams.get('username')

  useEffect(() => {
    fetch(`/confirm-sign-up?username=${username}`)
      .then(resp => resp.json())
      .then(data => {
        setSignUpCode(data.signUpCode)
        setView('form')
      })
  }, [])

  switch (view as View) {
    case 'none':
      return null
    case 'form':
      return <SignUpStep2Form error={error} signUpCode={signUpCode} submitFormCallback={submitFormCallback.bind(null, setView, setError)} />
    case 'success':
      return <SignUpStep2Success />
  }
}
