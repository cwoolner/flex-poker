import React, { useEffect } from 'react'
import { useDispatch } from 'react-redux'
import { changeChatMsgStream } from '../../reducers'

export default ({ match: { params: { gameId }} }) => {

  const dispatch = useDispatch()

  useEffect(() => {
    dispatch(changeChatMsgStream(gameId, null))
  }, [gameId])

  return <div>Game page</div>
}
