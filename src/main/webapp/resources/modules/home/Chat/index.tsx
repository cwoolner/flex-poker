import { useEffect, useRef } from 'react';
import { Form } from 'react-bootstrap'
import WebSocketService from '../../webSocket/WebSocketService'
import { useSelector } from 'react-redux'
import ChatLine from './ChatLine'
import { getChats } from './selectors'
import { RootState } from '../../..'

const displayAreaMutationObserverCallback = (displayArea, mutations) => {
  for (let mutation of mutations) {
    if (mutation.type == 'childList' && displayArea.current.lastChild) {
      displayArea.current.lastChild.scrollIntoView()
    }
  }
}

const chatFormSubmitted = (activeChatStream, evt) => {
  evt.preventDefault()
  const messageTextBox = evt.target.elements[0]
  if (messageTextBox.value) {
    const message = {
      message: messageTextBox.value,
      ...activeChatStream
    }
    WebSocketService.send('/app/sendchatmessage', message)
    messageTextBox.value = ''
  }
}

export default () => {

  const displayArea = useRef(null)

  const activeChatStream = useSelector((state: RootState) => state.activeChatStream)
  const chats = useSelector(state => getChats(state))

  useEffect(() => {
    const displayAreaMutationObserver = new MutationObserver(
      displayAreaMutationObserverCallback.bind(null, displayArea))
    displayAreaMutationObserver.observe(displayArea.current, { childList: true })
    if (displayArea.current.lastChild) {
      displayArea.current.lastChild.scrollIntoView()
    }

    return () => displayAreaMutationObserver.disconnect()
  }, [activeChatStream, displayArea])

  return (
    <div className="chat-area">
      <div className="chat-text-display-area form-control form-group" ref={displayArea}>
        {chats.map((msg) => <ChatLine key={msg.id} chat={msg} />)}
      </div>
      <form onSubmit={chatFormSubmitted.bind(null, activeChatStream)}>
        <Form.Group>
          <Form.Control type="text" placeholder="Chat..." />
        </Form.Group>
      </form>
    </div>
  )
}
