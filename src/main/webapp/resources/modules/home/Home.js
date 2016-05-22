import React from 'react';
import webSocketService from '../common/webSocketService';
import MainTabs from './MainTabs';
import CreateGameDialog from '../game/CreateGameDialog';

export default React.createClass({

  getInitialState() {
    return {
      createGameDialogOpen: false
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
      document.querySelector('fp-join-game-dialog').showDialog(gameId);
    });

    document.querySelector('fp-join-game-dialog').addEventListener('join-game-submitted', evt => {
      webSocketService.send('/app/joingame', evt.detail);
    });

  },

  openCreateGameModal() {
    this.setState({
      createGameDialogOpen: true
    });
  },

  hideCreateGameDialog() {
    this.setState({
      createGameDialogOpen: false
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
        <fp-join-game-dialog></fp-join-game-dialog>
      </div>
    )
  }
});

function displayChat(message) {
  document.querySelector('fp-chat').displayChat(message.body);
}
