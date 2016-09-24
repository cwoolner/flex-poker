import React from 'react';
import WebSocketService from '../webSocket/WebSocketService';
import WebSocketSubscriptionManager from '../webSocket/WebSocketSubscriptionManager';
import Chat from '../common/Chat';

export default React.createClass({

  componentDidMount() {
    const gameId = this.props.params.gameId;

    WebSocketSubscriptionManager.subscribe(this, [
      {location: `/topic/chat/game/${gameId}/user`, subscription: displayChat.bind(this)},
      {location: `/topic/chat/game/${gameId}/system`, subscription: displayChat.bind(this)}
    ]);
  },

  componentWillUnmount() {
    WebSocketSubscriptionManager.unsubscribe(this);
  },

  render() {
    return (
      <div>
        <Chat ref="gameChat" sendChat={sendChat.bind(this, this.props.params.gameId)} />
      </div>
    )
  }

})

function displayChat(message) {
  this.refs.gameChat.displayChat(message.body);
}

function sendChat(gameId, message) {
  const gameMessage = {
    message: message,
    receiverUsernames: null,
    gameId: gameId,
    tableId: null
  };

  WebSocketService.send('/app/sendchatmessage', gameMessage);
}
