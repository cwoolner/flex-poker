import React from 'react';
import WebSocketService from '../webSocket/WebSocketService';
import { hashHistory, Link } from 'react-router';

export default React.createClass({

  componentDidMount() {
    WebSocketService.registerSubscription('/user/queue/errors', message => {
      alert("Error " + message.body);
      window.tryingToJoinGameId = null;
    });

    WebSocketService.registerSubscription('/app/opengamesforuser',
      message => displayGameTabs.call(this, message));

    WebSocketService.registerSubscription('/user/queue/opengamesforuser', message => {
      displayGameTabs.call(this, message);
      if (window.tryingToJoinGameId != null) {
        hashHistory.push(`/game/${window.tryingToJoinGameId}`);
        window.tryingToJoinGameId = null;
      }
    });

    WebSocketService.registerSubscription('/user/queue/opentable', message => {
      const openTable = JSON.parse(message.body);
      window.location.hash = `/game/${openTable.gameId}/table/${openTable.tableId}`;
    });

    WebSocketService.registerSubscription('/user/queue/personaltablestatus', message => {
      alert(JSON.parse(message.body));
    });

    WebSocketService.registerSubscription('/user/queue/pocketcards', message => {
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
    });

  },

  render() {
    return (
      <div>
        <Link to="/logout">Logout</Link>
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
