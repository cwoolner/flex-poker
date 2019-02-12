import React, { useEffect } from 'react';
import CreateGameDialog from './CreateGameDialog';
import JoinGameDialog from './JoinGameDialog';
import GameList from './GameList';
import { connect } from 'react-redux'
import { changeChatMsgStream } from '../../reducers'

const Lobby = ({ changeChatMsgStream }) => {

  useEffect(() => {
    changeChatMsgStream()
  })

  return (
    <>
      <GameList />
      <CreateGameDialog />
      <JoinGameDialog />
    </>
  )
}

const mapDispatchToProps = (dispatch) => ({
  changeChatMsgStream: () => dispatch(changeChatMsgStream(null, null))
})

export default connect(null, mapDispatchToProps)(Lobby)
