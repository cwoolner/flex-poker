import React, { useEffect } from 'react'
import CreateGameDialog from './CreateGameDialog'
import JoinGameDialog from './JoinGameDialog'
import GameList from './GameList'
import { useDispatch } from 'react-redux'
import { changeChatMsgStream } from '../../reducers'

export default () => {
  const dispatch = useDispatch()

  useEffect(() => {
    dispatch(changeChatMsgStream(null, null))
  })

  return (
    <>
      <GameList />
      <CreateGameDialog />
      <JoinGameDialog />
    </>
  )
}
