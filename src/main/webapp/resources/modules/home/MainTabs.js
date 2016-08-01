import React from 'react';
import WebSocketSubscriptionManager from '../webSocket/WebSocketSubscriptionManager';
import { hashHistory, Link } from 'react-router';

export default React.createClass({

  componentDidMount() {
    const subscriptions = [];
    subscriptions.push({location: '/user/queue/errors', subscription: message => {
      alert("Error " + message.body);
      window.tryingToJoinGameId = null;
    }});
    subscriptions.push({location: '/app/opengamesforuser', subscription: message =>
      displayGameTabs.call(this, message)});
    subscriptions.push({location: '/user/queue/opengamesforuser', subscription: message => {
      displayGameTabs.call(this, message);
      if (window.tryingToJoinGameId != null) {
        hashHistory.push(`/game/${window.tryingToJoinGameId}`);
        window.tryingToJoinGameId = null;
      }
    }});
    subscriptions.push({location: '/user/queue/opentable', subscription: message => {
      const openTable = JSON.parse(message.body);
      window.location.hash = `/game/${openTable.gameId}/table/${openTable.tableId}`;
    }});
    subscriptions.push({location: '/user/queue/personaltablestatus', subscription: message =>
      alert(JSON.parse(message.body))});
    subscriptions.push({location: '/user/queue/pocketcards', subscription: message => {
      const parsedData = JSON.parse(message.body);
      const pocketCards = {
        cardId1: parsedData.cardId1,
        cardId2: parsedData.cardId2
      };

      const pocketCardsReceivedEvent = new CustomEvent(`pocketCardsReceived-${parsedData.tableId}`, {
        detail: pocketCards,
        bubbles: true
      });
      document.dispatchEvent(pocketCardsReceivedEvent);
    }});
    WebSocketSubscriptionManager.subscribe(this, subscriptions);
  },

  componentWillUnmount() {
    WebSocketSubscriptionManager.unsubscribe(this);
  },

  render() {
    return (
      <div>
        <ul className="game-tab-container">
          <li><Link to="/">Lobby</Link></li>
        </ul>
        {this.props.children}
      </div>
    )
  }
})

function displayGameTabs(message){
  let container = document.querySelector('.game-tab-container');

  while(container.childElementCount > 1) {
    container.removeChild(container.lastChild);
  }

  JSON.parse(message.body).forEach(openGame => {
    let gameAnchor = document.createElement('a');
    gameAnchor.innerHTML = openGame.name;
    gameAnchor.href = `#/game/${openGame.gameId}`;

    let gameTab = document.createElement('li');
    gameTab.className = `game-tab-${openGame.gameStage.toLowerCase()}`;

    gameTab.appendChild(gameAnchor);
    container.appendChild(gameTab);
  });
}
