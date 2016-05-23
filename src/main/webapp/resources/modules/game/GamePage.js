import React from 'react';
import webSocketService from '../common/webSocketService';

export default React.createClass({

  componentDidMount() {
    const gameId = this.props.params.gameId;
    webSocketService.registerSubscription(`/topic/chat/game/${gameId}/user`, displayChat.bind(this));
    webSocketService.registerSubscription(`/topic/chat/game/${gameId}/system`, displayChat.bind(this));
    document.querySelector('.game-chat').addEventListener('chat-msg-entered', evt => sendChat(evt.detail, gameId));
  },

  render() {
    return (
      <div>
        <p>{this.props.params.gameId}</p>
        <fp-chat class="game-chat"></fp-chat>
      </div>
    )
  }

})

function displayChat(message) {
  document.querySelector('fp-chat').displayChat(message.body);
}

function sendChat(message, gameId) {
  const gameMessage = {
    message: message,
    receiverUsernames: null,
    gameId: gameId,
    tableId: null
  };

  webSocketService.send('/app/sendchatmessage', gameMessage);
}
