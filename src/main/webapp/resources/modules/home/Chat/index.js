import React from 'react';
import { List, Map } from 'immutable'
import { FormGroup, FormControl, FieldGroup, Button } from 'react-bootstrap';
import WebSocketSubscriptionManager from '../../webSocket/WebSocketSubscriptionManager';
import WebSocketService from '../../webSocket/WebSocketService';
import { connect } from 'react-redux'
import { globalChatMsgReceived } from '../../../reducers';
import ChatLine from './ChatLine'

class Chat extends React.Component {

  constructor(props) {
    super(props);

    this.displayAreaMutationObserverCallback = this.displayAreaMutationObserverCallback.bind(this)
    this.chatFormSubmitted = this.chatFormSubmitted.bind(this);
    this.acceptGlobalChatMsg = this.acceptGlobalChatMsg.bind(this)

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

  acceptGlobalChatMsg(message) {
    this.props.dispatch(globalChatMsgReceived(JSON.parse(message.body)))
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
    this.displayAreaMutationObserver.observe(this.displayArea.current, { childList: true })

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
    this.displayAreaMutationObserver.disconnect();
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
    return { activeChatStream, chats: chatMessages.tableMessages
      .get(activeChatStream.gameId, Map())
      .get(activeChatStream.tableId, List()) }
  } else if (activeChatStream.gameId) {
    return { activeChatStream, chats: chatMessages.gameMessages
      .get(activeChatStream.gameId, List()) }
  } else {
    return { activeChatStream, chats: chatMessages.globalMessages }
  }}

export default connect(mapStateToProps)(Chat)
