import React from 'react';
import { List, Map } from 'immutable'
import { FormGroup, FormControl, FieldGroup, Button } from 'react-bootstrap';
import WebSocketService from '../../webSocket/WebSocketService';
import { connect } from 'react-redux'
import ChatLine from './ChatLine'

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
      if (mutation.type == 'childList') {
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
          <FormGroup>
            <FormControl type="text" placeholder="Chat..." />
          </FormGroup>
        </form>
      </div>
    )
  }

}

const mapStateToProps = state => {
  const { activeChatStream, chatMessages } = state
  if (activeChatStream.gameId && activeChatStream.tableId) {
    const { gameId, tableId } = activeChatStream
    return { activeChatStream, chats: chatMessages.tableMessages.get(tableId, List()) }
  } else if (activeChatStream.gameId) {
    const { gameId } = activeChatStream
    return { activeChatStream, chats: chatMessages.gameMessages.get(gameId, List()) }
  } else {
    return { activeChatStream, chats: chatMessages.globalMessages }
  }}

export default connect(mapStateToProps)(Chat)
