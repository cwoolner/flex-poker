import React from 'react';
import webSocketService from '../common/webSocketService';
import MainTabs from './MainTabs';
import CreateGameDialog from '../game/CreateGameDialog';
import JoinGameDialog from '../game/JoinGameDialog';
import GameList from '../game/GameList';
import Chat from '../common/Chat';
import { Link } from 'react-router';

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
        <Link to="/logout">Logout</Link>
        <MainTabs />
        <button className={'btn'} onClick={this.openCreateGameModal}>Create Game</button>
        <GameList
          gameList={this.state.openGameList}
          gameOpenedCallback={this.gameOpened}
          className={'game-list'} />
        <Chat ref="globalChat" sendChat={sendGlobalChat} />
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
  this.refs.globalChat.displayChat(message.body);
}

function sendGlobalChat(message) {
  const globalMessage = {
    message,
    receiverUsernames: null,
    gameId: null,
    tableId: null
  };

  webSocketService.send('/app/sendchatmessage', globalMessage);
}
