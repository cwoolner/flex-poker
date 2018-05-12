import React from 'react';
import { connect } from 'react-redux'
import { changeChatMsgStream } from '../../reducers'

class GamePage extends React.Component {

  constructor(props) {
    super(props)
  }

  componentDidMount() {
    const { gameId } = this.props.match.params
    this.props.changeChatMsgStream(gameId)
  }

  componentDidUpdate(prevProps, prevState, snapshot) {
    const prevGameId = prevProps.match.params.gameId
    const currentGameId = this.props.match.params.gameId

    if (prevGameId !== currentGameId) {
      this.props.changeChatMsgStream(currentGameId)
    }
  }

  render() {
    return <div>Game page</div>
  }

}

const mapDispatchToProps = dispatch => ({
  changeChatMsgStream: gameId => dispatch(changeChatMsgStream(gameId, null))
})

export default connect(null, mapDispatchToProps)(GamePage)
