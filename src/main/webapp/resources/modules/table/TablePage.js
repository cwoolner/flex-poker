import React, { useEffect } from 'react'
import { Map } from 'immutable'
import CommonCards from './CommonCards'
import MyCards from './MyCards'
import Seat from './Seat'
import PokerActions from './PokerActions'
import SeatContainer from './SeatContainer'
import _ from 'lodash'
import { connect } from 'react-redux'
import { changeChatMsgStream, changeTable } from '../../reducers'

const TablePage = ({ match: { params: { gameId, tableId }}, changeChatMsgStream,
  changeTable, seats, totalPot, visibleCommonCards, pots, cardId1, cardId2 }) => {

  useEffect(() => {
    changeChatMsgStream(gameId, tableId)
  }, [gameId, tableId])

  useEffect(() => {
    changeTable(gameId, tableId)
  }, [gameId, tableId])

  const username = window.username
  const mySeat = seats.find(seat => seat.name === username)

  return (
    <>
      <div className="poker-table">
        <div>{totalPot}</div>
        <CommonCards visibleCommonCards={visibleCommonCards} />
        <SeatContainer gameId={gameId} tableId={tableId} mySeat={mySeat} seats={seats} />
      </div>

      {pots.map(pot => {
        return(
          <div key={pot}>
            <div>{pot.seats}</div>
            <div>{pot.amount}</div>
            <div>{pot.winners}</div>
            <div>{pot.open ? 'open' : 'closed'}</div>
          </div>
        )
      })}

      <MyCards myLeftCardId={cardId1} myRightCardId={cardId2} />
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
    </>
  )
}

const mapStateToProps = (state) => {
  const tableData = state.tables.get(state.activeTable.gameId, Map()).get(state.activeTable.tableId)
  if (tableData) {
    const pocketCardData = state.pocketCards.get(tableData.currentHandId)
    return {
      ...tableData,
      ...pocketCardData
    }
  } else {
    return {
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

const mapDispatchToProps = (dispatch) => ({
  changeChatMsgStream: (gameId, tableId) => dispatch(changeChatMsgStream(gameId, tableId)),
  changeTable: (gameId, tableId) => dispatch(changeTable(gameId, tableId))
})

export default connect(mapStateToProps, mapDispatchToProps)(TablePage)
