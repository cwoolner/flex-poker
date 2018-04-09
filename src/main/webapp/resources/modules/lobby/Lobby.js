import React from 'react';
import WebSocketService from '../webSocket/WebSocketService';
import WebSocketSubscriptionManager from '../webSocket/WebSocketSubscriptionManager';
import CreateGameDialog from './CreateGameDialog';
import JoinGameDialog from './JoinGameDialog';
import GameList from './GameList';
import Chat from '../common/Chat';
import { Button } from 'react-bootstrap';
import { connect } from 'react-redux'

class Lobby extends React.Component {

  constructor(props) {
    super(props)

    this.displayChat = this.displayChat.bind(this)
  }

  componentDidMount() {
    WebSocketSubscriptionManager.subscribe(this, [
      {location: '/topic/chat/global/user', subscription: this.displayChat},
      {location: '/topic/chat/global/system', subscription: this.displayChat}
    ]);
  }

  componentWillUnmount() {
    WebSocketSubscriptionManager.unsubscribe(this);
  }

  displayChat(message) {
    this.refs.globalChat.displayChat(message.body)
  }

  sendGlobalChat(message) {
    const globalMessage = {
      message,
      receiverUsernames: null,
      gameId: null,
      tableId: null
    }
    WebSocketService.send('/app/sendchatmessage', globalMessage)
  }

  render() {
    return (
      <div>
        <GameList />
        <Chat ref="globalChat" sendChat={this.sendGlobalChat} />
        <CreateGameDialog />
        <JoinGameDialog />
      </div>
    )
  }

}

export default Lobby
