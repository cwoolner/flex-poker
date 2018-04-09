import React from 'react'
import GameList from './GameList'
import { connect } from 'react-redux'
import { showJoinGameModal, showCreateGameModal } from '../../../reducers'

const GameListContainer = ({gameList, dispatch}) => {
  return (
    <GameList gameList={gameList}
              gameOpenedCallback={joinGameId => dispatch(showJoinGameModal(joinGameId))}
              openCreateGameModalCallback={() => dispatch(showCreateGameModal())} />
  )
}

const mapStateToProps = state => ({gameList: state.openGameList})

export default connect(mapStateToProps)(GameListContainer)
