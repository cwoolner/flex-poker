import React from 'react';
import WebSocketSubscriptionManager from '../webSocket/WebSocketSubscriptionManager';
import CreateGameDialog from './CreateGameDialog';
import JoinGameDialog from './JoinGameDialog';
import GameList from './GameList';
import { Button } from 'react-bootstrap';
import { connect } from 'react-redux'
import { changeChatMsgStream } from '../../reducers'


class Lobby extends React.Component {

  constructor(props) {
    super(props)
  }

  componentDidMount() {
    WebSocketSubscriptionManager.subscribe(this, [
      {location: '/topic/availabletournaments', subscription: this.updateGameList}
    ]);
    this.props.changeChatMsgStream()
  }

  componentWillUnmount() {
    WebSocketSubscriptionManager.unsubscribe(this);
  }

  render() {
    return (
      <div>
        <GameList />
        <CreateGameDialog />
        <JoinGameDialog />
      </div>
    )
  }

}

const mapDispatchToProps = dispatch => ({
  changeChatMsgStream: () => dispatch(changeChatMsgStream(null, null))
})

export default connect(null, mapDispatchToProps)(Lobby)
