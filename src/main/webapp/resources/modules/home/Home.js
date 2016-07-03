import React from 'react';
import WebSocketService from '../webSocket/WebSocketService';
import WebSocketSubscriptionManager from '../webSocket/WebSocketSubscriptionManager';
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
    const subscriptions = [];
    subscriptions.push({location: '/topic/chat/global/user', subscription: displayChat.bind(this)});
    subscriptions.push({location: '/topic/chat/global/system', subscription: displayChat.bind(this)});
    subscriptions.push({location: '/topic/availabletournaments', subscription: message => {
      this.setState({
        openGameList: JSON.parse(message.body)
      });
    }});

    WebSocketSubscriptionManager.subscribe(this, subscriptions);
  },

  componentWillUnmount() {
    WebSocketSubscriptionManager.unsubscribe(this);
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
