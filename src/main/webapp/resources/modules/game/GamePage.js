import React, { useEffect } from 'react'
import { connect } from 'react-redux'
import { changeChatMsgStream } from '../../reducers'

const GamePage = ({ changeChatMsgStream, match: { params: { gameId }} }) => {

  useEffect(() => {
    changeChatMsgStream(gameId)
  }, [gameId])

  return <div>Game page</div>
}

const mapDispatchToProps = (dispatch) => ({
  changeChatMsgStream: (gameId) => dispatch(changeChatMsgStream(gameId, null))
})

export default connect(null, mapDispatchToProps)(GamePage)
