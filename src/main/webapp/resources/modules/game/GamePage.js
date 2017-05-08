import React from 'react';
import WebSocketService from '../webSocket/WebSocketService';
import WebSocketSubscriptionManager from '../webSocket/WebSocketSubscriptionManager';
import Chat from '../common/Chat';

class GamePage extends React.Component {

  constructor(props) {
    super(props)
    this.displayChat = this.displayChat.bind(this)
    this.sendChat = this.sendChat.bind(this)
  }

  componentDidMount() {
    const gameId = this.props.params.gameId;

    WebSocketSubscriptionManager.subscribe(this, [
      {location: `/topic/chat/game/${gameId}/user`, subscription: this.displayChat},
      {location: `/topic/chat/game/${gameId}/system`, subscription: this.displayChat}
    ]);
  }

  componentWillUnmount() {
    WebSocketSubscriptionManager.unsubscribe(this);
  }

  displayChat(message) {
    this.refs.gameChat.displayChat(message.body);
  }

  sendChat(message) {
    const gameMessage = {
      message: message,
      receiverUsernames: null,
      gameId: this.props.params.gameId,
    }
    WebSocketService.send('/app/sendchatmessage', gameMessage)
  }

  render() {
    return (
      <div>
        <Chat ref="gameChat" sendChat={this.sendChat} />
      </div>
    )
  }

}

export default GamePage
