import React from 'react';
import CreateGameDialog from './CreateGameDialog';
import JoinGameDialog from './JoinGameDialog';
import GameList from './GameList';
import Button from 'react-bootstrap/Button';
import { connect } from 'react-redux'
import { changeChatMsgStream } from '../../reducers'


class Lobby extends React.Component {

  constructor(props) {
    super(props)
  }

  componentDidMount() {
    this.props.changeChatMsgStream()
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
