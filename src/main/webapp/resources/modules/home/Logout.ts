import WebSocketService from '../webSocket/WebSocketService'

export default () => {
  WebSocketService.disconnect()

  const headerElem: HTMLMetaElement = document.querySelector("meta[name='_csrf_header']")
  const tokenElem: HTMLMetaElement = document.querySelector("meta[name='_csrf']")

  const myHeaders = new Headers()
  myHeaders.append(headerElem.content, tokenElem.content)

  const myInit: RequestInit = {
    method: 'POST',
    headers: myHeaders,
    cache: 'no-cache',
    redirect: 'follow',
    credentials: 'same-origin'
  }

  fetch('/logout', myInit).then(response => location.href = response.url)
}
