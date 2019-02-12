import React, { useEffect, useRef } from 'react'
import Form from 'react-bootstrap/Form'
import WebSocketService from '../../webSocket/WebSocketService'
import { connect } from 'react-redux'
import ChatLine from './ChatLine'
import { getChats } from './selectors'

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

const Chat = ({ activeChatStream, chats }) => {

  const displayArea = useRef(null)

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

const mapStateToProps = (state) => ({
   activeChatStream: state.activeChatStream,
   chats: getChats(state)
 })

export default connect(mapStateToProps)(Chat)
