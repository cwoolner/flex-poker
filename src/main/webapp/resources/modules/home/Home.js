import React from 'react';
import webSocketService from '../common/webSocketService';
import MainTabs from './MainTabs';
import CreateGameDialog from '../game/CreateGameDialog';
import JoinGameDialog from '../game/JoinGameDialog';

export default React.createClass({

  getInitialState() {
    return {
      createGameDialogOpen: false,
      joinGameDialogOpen: false,
      joinGameId: null
    }
  },

  componentDidMount() {
    webSocketService.registerSubscription('/topic/chat/global/user', displayChat.bind(this));
    webSocketService.registerSubscription('/topic/chat/global/system', displayChat.bind(this));

    webSocketService.registerSubscription('/topic/availabletournaments', message => {
      document.querySelector('fp-gamelist').displayGames(JSON.parse(message.body));
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

    document.querySelector('fp-gamelist').addEventListener('game-open-selected', evt => {
      const gameId = evt.detail;
      window.tryingToJoinGameId = gameId;
      this.openJoinGameModal(gameId);
    });

  },

  openCreateGameModal() {
    this.setState({
      createGameDialogOpen: true,
      joinGameDialogOpen: this.state.joinGameDialogOpen,
      joinGameId: null
    });
  },

  openJoinGameModal(gameId) {
    this.setState({
      createGameDialogOpen: this.state.createGameDialogOpen,
      joinGameDialogOpen: true,
      joinGameId: gameId
    });
  },

  hideCreateGameDialog() {
    this.setState({
      createGameDialogOpen: false,
      joinGameDialogOpen: this.state.joinGameDialogOpen,
      joinGameId: null
    });
  },

  hideJoinGameDialog() {
    this.setState({
      createGameDialogOpen: this.state.createGameDialogOpen,
      joinGameDialogOpen: false,
      joinGameId: null
    });
  },

  render() {
    return (
      <div>
        <MainTabs />
        <button onClick={this.openCreateGameModal}>Create Game</button>
        <fp-gamelist></fp-gamelist>
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
