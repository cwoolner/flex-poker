import webstomp, { Client, Message } from 'webstomp-client'
import SockJS from 'sockjs-client'

const WebSocketService = () => {
  let client: Client;

  const registerSubscription = (location: string, subscription: (message: Message) => any) => {
    return new Promise((resolve, reject) => {
      if (client.connected) {
        resolve(client.subscribe(location, subscription))
      } else {
        document.addEventListener('webSocketConnected', evt => {
          resolve(client.subscribe(location, subscription))
        })
      }
    })
  }

  const send = (location: string, objectToSend) => client.send(location, JSON.stringify(objectToSend))

  const connect = () => {
    client = webstomp.over(new SockJS('/application'), {debug: false})
    client.connect({}, frame => document.dispatchEvent(new Event('webSocketConnected')))
  }

  const disconnect = () => client.disconnect()

  return { registerSubscription, send, connect, disconnect }
}

export default WebSocketService()
