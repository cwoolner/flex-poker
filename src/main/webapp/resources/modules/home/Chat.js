import React from 'react';
import { FormGroup, FormControl, FieldGroup, Button } from 'react-bootstrap';
import WebSocketSubscriptionManager from '../webSocket/WebSocketSubscriptionManager';
import WebSocketService from '../webSocket/WebSocketService';

class Chat extends React.Component {

  constructor(props) {
    super(props);
    this.chatFormSubmitted = this.chatFormSubmitted.bind(this);
    this.displayChat = this.displayChat.bind(this)
  }

  displayChat(message) {
    const chatTextArea = document.querySelector('#chat-text');
    const scrollHeight = chatTextArea['scrollHeight'];
    chatTextArea['scrollTop'] = scrollHeight;
    chatTextArea.value += message.body + '\n';
  }

  chatFormSubmitted(evt) {
    evt.preventDefault();
    const messageTextBox = evt.target.elements[0];
    if (messageTextBox.value) {
      this.sendGlobalChat(messageTextBox.value);
      messageTextBox.value = '';
    }
  }

  componentDidMount() {
    const gameId = '';
    const tableId = '';

    WebSocketSubscriptionManager.subscribe(this, [
      {location: '/topic/chat/global/user', subscription: this.displayChat},
      {location: '/topic/chat/global/system', subscription: this.displayChat},
      {location: `/topic/chat/game/${gameId}/user`, subscription: this.displayChat},
      {location: `/topic/chat/game/${gameId}/system`, subscription: this.displayChat},
      {location: `/topic/chat/game/${gameId}/table/${tableId}/user`, subscription: this.displayChat},
      {location: `/topic/chat/game/${gameId}/table/${tableId}/system`, subscription: this.displayChat}
    ])
  }

  componentWillUnmount() {
    WebSocketSubscriptionManager.unsubscribe(this);
  }

  sendGlobalChat(message) {
    const globalMessage = {
      message,
      receiverUsernames: null,
      gameId: null,
      tableId: null
    };

    WebSocketService.send('/app/sendchatmessage', globalMessage);
  }

  sendGameChat(gameId, message) {
    const gameMessage = {
      message: message,
      receiverUsernames: null,
      gameId: gameId,
      tableId: null
    };

    WebSocketService.send('/app/sendchatmessage', gameMessage);
  }

  sendTableChat(gameId, tableId, message) {
    const tableMessage = {
      message: message,
      receiverUsernames: null,
      gameId: gameId,
      tableId: tableId
    };

    WebSocketService.send('/app/sendchatmessage', tableMessage);
  }

  render() {
    return (
      <div className={'chat-area'}>
        <FormGroup>
          <FormControl componentClass="textarea" id="chat-text" disabled="disabled" />
        </FormGroup>
        <form onSubmit={this.chatFormSubmitted}>
          <FormGroup>
            <FormControl type="text" placeholder="Chat..." />
          </FormGroup>
        </form>
      </div>
    )
  }

}

export default Chat
