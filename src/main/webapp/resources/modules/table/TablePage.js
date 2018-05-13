import React from 'react';
import { Map } from 'immutable'
import CommonCards from './CommonCards';
import MyCards from './MyCards';
import Seat from './Seat';
import PokerActions from './PokerActions';
import SeatContainer from './SeatContainer';
import _ from 'lodash';
import { connect } from 'react-redux'
import { changeChatMsgStream, changeTable } from '../../reducers'

class TablePage extends React.Component {

  constructor(props) {
    super(props)
  }

  componentDidMount() {
    const { gameId, tableId } = this.props.match.params
    this.props.changeChatMsgStream(gameId, tableId)
    this.props.changeTable(gameId, tableId)
  }

  componentDidUpdate(prevProps, prevState, snapshot) {
    const prevTableId = prevProps.match.params.tableId
    const currentTableId = this.props.match.params.tableId

    if (prevTableId !== currentTableId) {
      const { gameId, tableId } = this.props.match.params
      this.props.changeChatMsgStream(gameId, tableId)
      this.props.changeTable(gameId, tableId)
    }
  }

  render() {
    const { gameId, tableId } = this.props.match.params
    const username = window.username;
    const mySeat = this.props.seats.find(seat => seat.name === username);

    return (
      <div>
        <div className={"poker-table"}>
          <div>{this.props.totalPot}</div>
          <CommonCards visibleCommonCards={this.props.visibleCommonCards} />
          <SeatContainer gameId={gameId} tableId={tableId} mySeat={mySeat} seats={this.props.seats} />
        </div>

        {this.props.pots.map(pot => {
          return(
            <div>
              <div>{pot.seats}</div>
              <div>{pot.amount}</div>
              <div>{pot.winners}</div>
              <div>{pot.open ? 'open' : 'closed'}</div>
            </div>
          )
        })}

        <MyCards myLeftCardId={this.props.cardId1} myRightCardId={this.props.cardId2} />
        {
          _.isNil(mySeat)
            ? null
            : <PokerActions
                gameId={gameId}
                tableId={tableId}
                actionOn={mySeat.actionOn}
                callAmount={mySeat.callAmount}
                minRaiseTo={mySeat.raiseTo}
                maxRaiseTo={mySeat.chipsInBack + mySeat.chipsInFront} />
        }
      </div>
    )
  }

}

const mapStateToProps = state => {
  const tableData = state.tables.get(state.activeTable.gameId, Map()).get(state.activeTable.tableId)
  const pocketCardData = state.pocketCards.get(state.activeTable.tableId)
  if (tableData) {
    return {
      ...tableData,
      ...pocketCardData
    }
  } else {
    return {
      myRightCardId: null,
      totalPot: 0,
      visibleCommonCards: [],
      seats: [],
      tableVersion: 0,
      pots: [],
      cardId1: null,
      cardId2: null
    }
  }
}

const mapDispatchToProps = dispatch => ({
  changeChatMsgStream: (gameId, tableId) => dispatch(changeChatMsgStream(gameId, tableId)),
  changeTable: (gameId, tableId) => dispatch(changeTable(gameId, tableId))
})

export default connect(mapStateToProps, mapDispatchToProps)(TablePage)
