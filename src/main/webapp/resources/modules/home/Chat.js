import React from 'react';
import { FormGroup, FormControl, FieldGroup, Button } from 'react-bootstrap';
import WebSocketSubscriptionManager from '../webSocket/WebSocketSubscriptionManager';
import WebSocketService from '../webSocket/WebSocketService';
import { connect } from 'react-redux'
import { globalChatMsgReceived } from '../../reducers';

class Chat extends React.Component {

  constructor(props) {
    super(props);
    this.chatFormSubmitted = this.chatFormSubmitted.bind(this);
    this.displayChat = this.displayChat.bind(this)
    this.acceptGlobalChatMsg = this.acceptGlobalChatMsg.bind(this)
  }

  acceptGlobalChatMsg(message) {
    this.props.dispatch(globalChatMsgReceived(message.body))
  }

  displayChat(message) {
    return message.toArray().join('\n')
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
      {location: '/topic/chat/global', subscription: this.acceptGlobalChatMsg}
//      {location: `/topic/chat/game/${gameId}`, subscription: this.displayChat},
//      {location: `/topic/chat/game/${gameId}/table/${tableId}`, subscription: this.displayChat}
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
          <FormControl componentClass="textarea" id="chat-text" disabled="disabled" value={this.displayChat(this.props.globalMessages)} />
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

const mapStateToProps = state => ({ globalMessages: state.chatMessages.globalMessages })

export default connect(mapStateToProps)(Chat)
