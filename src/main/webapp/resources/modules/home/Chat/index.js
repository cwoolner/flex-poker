import React from 'react';
import { List, Map } from 'immutable'
import Button from 'react-bootstrap/Button'
import Form from 'react-bootstrap/Form'
import WebSocketService from '../../webSocket/WebSocketService'
import { connect } from 'react-redux'
import ChatLine from './ChatLine'
import { getChats } from './selectors'

class Chat extends React.Component {

  constructor(props) {
    super(props);

    this.displayAreaMutationObserverCallback = this.displayAreaMutationObserverCallback.bind(this)
    this.chatFormSubmitted = this.chatFormSubmitted.bind(this);

    this.displayArea = React.createRef()
    this.displayAreaMutationObserver = new MutationObserver(this.displayAreaMutationObserverCallback);
  }

  displayAreaMutationObserverCallback(mutations) {
    for (let mutation of mutations) {
      if (mutation.type == 'childList' && this.displayArea.current.lastChild) {
        this.displayArea.current.lastChild.scrollIntoView();
      }
    }
  }

  chatFormSubmitted(evt) {
    evt.preventDefault();
    const messageTextBox = evt.target.elements[0];
    if (messageTextBox.value) {
      const message = {
        message: messageTextBox.value,
        ...this.props.activeChatStream
      }
      WebSocketService.send('/app/sendchatmessage', message);
      messageTextBox.value = '';
    }
  }

  componentDidMount() {
    this.displayAreaMutationObserver.observe(this.displayArea.current, { childList: true })
  }

  componentWillUnmount() {
    this.displayAreaMutationObserver.disconnect();
  }

  render() {
    return (
      <div className={'chat-area'}>
        <div className={'chat-text-display-area form-control form-group'} ref={this.displayArea}>
          {this.props.chats.map(msg => <ChatLine chat={msg} />)}
        </div>
        <form onSubmit={this.chatFormSubmitted}>
          <Form.Group>
            <Form.Control type="text" placeholder="Chat..." />
          </Form.Group>
        </form>
      </div>
    )
  }

}

const mapStateToProps = state => ({
   activeChatStream: state.activeChatStream,
   chats: getChats(state)
 })

export default connect(mapStateToProps)(Chat)
