import React from 'react';
import WebSocketService from '../webSocket/WebSocketService';
import WebSocketSubscriptionManager from '../webSocket/WebSocketSubscriptionManager';
import { connect } from 'react-redux'
import { changeChatMsgStream } from '../../reducers'

class GamePage extends React.Component {

  constructor(props) {
    super(props)
  }

  componentDidMount() {
    const { gameId } = this.props.match.params
    WebSocketSubscriptionManager.subscribe(this, []);
    this.props.dispatch(changeChatMsgStream(gameId, null))
  }

  componentWillUnmount() {
    WebSocketSubscriptionManager.unsubscribe(this);
  }

  render() {
    return <div>Game page</div>
  }

}

export default connect()(GamePage)
