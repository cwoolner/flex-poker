import React from 'react';
import webSocketService from '../common/webSocketService';
import MainTabs from './MainTabs';
import CreateGameDialog from '../game/CreateGameDialog';
import JoinGameDialog from '../game/JoinGameDialog';
import GameList from '../game/GameList';

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
    webSocketService.registerSubscription('/topic/chat/global/user', displayChat.bind(this));
    webSocketService.registerSubscription('/topic/chat/global/system', displayChat.bind(this));

    webSocketService.registerSubscription('/topic/availabletournaments', message => {
      this.setState({
        createGameDialogOpen: this.state.createGameDialogOpen,
        joinGameDialogOpen: this.state.joinGameDialogOpen,
        joinGameId: this.state.joinGameId,
        openGameList: JSON.parse(message.body)
      });
    });

    document.querySelector('.global-chat').addEventListener('chat-msg-entered', evt => {
      const globalMessage = {
          message: evt.detail,
          receiverUsernames: null,
          gameId: null,
          tableId: null
      };

      webSocketService.send('/app/sendchatmessage', globalMessage);
    });

  },

  gameOpened(gameId) {
    window.tryingToJoinGameId = gameId;
    this.openJoinGameModal(gameId);
  },

  openCreateGameModal() {
    this.setState({
      createGameDialogOpen: true,
      joinGameDialogOpen: this.state.joinGameDialogOpen,
      joinGameId: null,
      openGameList: this.state.openGameList
    });
  },

  openJoinGameModal(gameId) {
    this.setState({
      createGameDialogOpen: this.state.createGameDialogOpen,
      joinGameDialogOpen: true,
      joinGameId: gameId,
      openGameList: this.state.openGameList
    });
  },

  hideCreateGameDialog() {
    this.setState({
      createGameDialogOpen: false,
      joinGameDialogOpen: this.state.joinGameDialogOpen,
      joinGameId: null,
      openGameList: this.state.openGameList
    });
  },

  hideJoinGameDialog() {
    this.setState({
      createGameDialogOpen: this.state.createGameDialogOpen,
      joinGameDialogOpen: false,
      joinGameId: null,
      openGameList: this.state.openGameList
    });
  },

  render() {
    return (
      <div>
        <MainTabs />
        <button className={'btn'} onClick={this.openCreateGameModal}>Create Game</button>
        <GameList
          gameList={this.state.openGameList}
          gameOpenedCallback={this.gameOpened}
          className={'game-list'} />
        <fp-chat class="global-chat"></fp-chat>
        <CreateGameDialog
          hideDialog={this.hideCreateGameDialog}
          className={this.state.createGameDialogOpen ? '' : 'hidden'} />
        <JoinGameDialog
          hideDialog={this.hideJoinGameDialog}
          className={this.state.joinGameDialogOpen ? '' : 'hidden'}
          gameId={this.state.joinGameId} />
      </div>
    )
  }
});

function displayChat(message) {
  document.querySelector('fp-chat').displayChat(message.body);
}
