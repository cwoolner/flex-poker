import React from 'react';
import WebSocketService from '../webSocket/WebSocketService';
import Chat from '../common/Chat';

export default React.createClass({

  componentDidMount() {
    const gameId = this.props.params.gameId;
    WebSocketService.registerSubscription(`/topic/chat/game/${gameId}/user`, displayChat.bind(this));
    WebSocketService.registerSubscription(`/topic/chat/game/${gameId}/system`, displayChat.bind(this));
  },

  render() {
    return (
      <div>
        <p>{this.props.params.gameId}</p>
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
