import React from 'react';
import WebSocketService from '../webSocket/WebSocketService';
import WebSocketSubscriptionManager from '../webSocket/WebSocketSubscriptionManager';

class GamePage extends React.Component {

  constructor(props) {
    super(props)
  }

  componentDidMount() {
    WebSocketSubscriptionManager.subscribe(this, []);
  }

  componentWillUnmount() {
    WebSocketSubscriptionManager.unsubscribe(this);
  }

  render() {
    return <div>Game page</div>
  }

}

export default GamePage
