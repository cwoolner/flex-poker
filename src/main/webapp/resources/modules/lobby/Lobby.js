import React from 'react';
import WebSocketService from '../webSocket/WebSocketService';
import WebSocketSubscriptionManager from '../webSocket/WebSocketSubscriptionManager';
import CreateGameDialog from './CreateGameDialog';
import JoinGameDialog from './JoinGameDialog';
import GameList from './GameList';
import Chat from '../common/Chat';
import { Button } from 'react-bootstrap';

class Lobby extends React.Component {

  constructor(props) {
    super(props)

    this.displayChat = this.displayChat.bind(this)
    this.updateGameList = this.updateGameList.bind(this)
    this.openCreateGameModal = this.openCreateGameModal.bind(this)
    this.openJoinGameModal = this.openJoinGameModal.bind(this)
    this.hideCreateGameDialog = this.hideCreateGameDialog.bind(this)
    this.hideJoinGameDialog = this.hideJoinGameDialog.bind(this)

    this.state = {
      createGameDialogOpen: false,
      joinGameDialogOpen: false,
      joinGameId: null,
      openGameList: []
    }
  }

  componentDidMount() {
    WebSocketSubscriptionManager.subscribe(this, [
      {location: '/topic/chat/global/user', subscription: this.displayChat},
      {location: '/topic/chat/global/system', subscription: this.displayChat},
      {location: '/topic/availabletournaments', subscription: this.updateGameList}
    ]);
  }

  componentWillUnmount() {
    WebSocketSubscriptionManager.unsubscribe(this);
  }

  openCreateGameModal() {
    this.setState({
      createGameDialogOpen: true,
      joinGameId: null
    });
  }

  openJoinGameModal(gameId) {
    this.setState({
      joinGameDialogOpen: true,
      joinGameId: gameId
    });
  }

  hideCreateGameDialog() {
    this.setState({
      createGameDialogOpen: false,
      joinGameId: null
    });
  }

  hideJoinGameDialog() {
    this.setState({
      joinGameDialogOpen: false,
      joinGameId: null
    });
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

  updateGameList(message) {
    this.setState({
      openGameList: JSON.parse(message.body)
    })
  }

  render() {
    return (
      <div>
        <GameList
          gameList={this.state.openGameList}
          gameOpenedCallback={this.openJoinGameModal}
          openCreateGameModalCallback={this.openCreateGameModal} />
        <Chat ref="globalChat" sendChat={this.sendGlobalChat} />
        <CreateGameDialog
          hideDialog={this.hideCreateGameDialog}
          showModal={this.state.createGameDialogOpen} />
        <JoinGameDialog
          hideDialog={this.hideJoinGameDialog}
          showModal={this.state.joinGameDialogOpen}
          gameId={this.state.joinGameId} />
      </div>
    )
  }

}

export default Lobby
