import React from 'react';
import WebSocketService from '../webSocket/WebSocketService';
import MainTabs from './MainTabs';
import CreateGameDialog from '../game/CreateGameDialog';
import JoinGameDialog from '../game/JoinGameDialog';
import GameList from '../game/GameList';
import Chat from '../common/Chat';

export default React.createClass({

  getInitialState() {
    return {
      createGameDialogOpen: false,
      joinGameDialogOpen: false,
      joinGameId: null,
      openGameList: []
    }
  },

  componentDidMount() {
    WebSocketService.registerSubscription('/topic/chat/global/user', displayChat.bind(this));
    WebSocketService.registerSubscription('/topic/chat/global/system', displayChat.bind(this));

    WebSocketService.registerSubscription('/topic/availabletournaments', message => {
      this.setState({
        openGameList: JSON.parse(message.body)
      });
    });

  },

  gameOpened(gameId) {
    window.tryingToJoinGameId = gameId;
    this.openJoinGameModal(gameId);
  },

  openCreateGameModal() {
    this.setState({
      createGameDialogOpen: true,
      joinGameId: null
    });
  },

  openJoinGameModal(gameId) {
    this.setState({
      joinGameDialogOpen: true,
      joinGameId: gameId
    });
  },

  hideCreateGameDialog() {
    this.setState({
      createGameDialogOpen: false,
      joinGameId: null
    });
  },

  hideJoinGameDialog() {
    this.setState({
      joinGameDialogOpen: false,
      joinGameId: null
    });
  },

  render() {
    return (
      <div>
        <button className={'btn'} onClick={this.openCreateGameModal}>Create Game</button>
        <GameList
          gameList={this.state.openGameList}
          gameOpenedCallback={this.gameOpened} />
        <Chat ref="globalChat" sendChat={sendGlobalChat} />
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
});

function displayChat(message) {
  this.refs.globalChat.displayChat(message.body);
}

function sendGlobalChat(message) {
  const globalMessage = {
    message,
    receiverUsernames: null,
    gameId: null,
    tableId: null
  };

  WebSocketService.send('/app/sendchatmessage', globalMessage);
}
