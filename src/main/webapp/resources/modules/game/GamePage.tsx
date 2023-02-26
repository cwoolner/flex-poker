import React, { useEffect } from 'react'
import { useDispatch } from 'react-redux'
import { changeChatMsgStream } from '../../reducers'
import { useParams } from 'react-router-dom'

export default () => {
  const { gameId } = useParams()
  const dispatch = useDispatch()

  useEffect(() => {
    dispatch(changeChatMsgStream(gameId, null))
  }, [gameId])

  return <div>Game page</div>
}
