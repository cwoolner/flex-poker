import WebSocketService from '../webSocket/WebSocketService'

export default () => {
  WebSocketService.disconnect()

  const header = document.querySelector("meta[name='_csrf_header']").content
  const token = document.querySelector("meta[name='_csrf']").content

  const myHeaders = new Headers()
  myHeaders.append(header, token)

  const myInit = {
    method: 'POST',
    headers: myHeaders,
    cache: 'no-cache',
    redirect: 'follow',
    credentials: 'same-origin'
  }

  fetch('/logout', myInit).then(response => location.href = response.url)
}
