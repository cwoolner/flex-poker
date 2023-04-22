import { useEffect } from 'react';
import { Map } from 'immutable'
import CommonCards from './CommonCards'
import MyCards from './MyCards'
import Seat from './Seat'
import PokerActions from './PokerActions'
import SeatContainer from './SeatContainer'
import _ from 'lodash'
import { useDispatch, useSelector } from 'react-redux'
import { changeChatMsgStream, changeTable } from '../../reducers'
import { useParams } from 'react-router-dom'
import { RootState } from '../..'

export default () => {

  const { gameId, tableId } = useParams()

  const { totalPot, visibleCommonCards, seats, tableVersion, pots, cardId1, cardId2 } = useSelector((state: RootState) => {
    const tableData = state.tables.get(state.activeTable.gameId, Map()).get(state.activeTable.tableId)
      || { totalPot: 0, visibleCommonCards: [], seats: [], tableVersion: 0, pots: [] }
    const pocketCardData = state.pocketCards.get(tableData.currentHandId)
      || { cardId1: null, cardId2: null }

    return {
      ...tableData,
      ...pocketCardData
    }
  })

  const dispatch = useDispatch()

  useEffect(() => {
    dispatch(changeChatMsgStream(gameId, tableId))
  }, [gameId, tableId])

  useEffect(() => {
    dispatch(changeTable(gameId, tableId))
  }, [gameId, tableId])

  // @ts-expect-error
  const username = window.username
  const mySeat = seats.find(seat => seat.name === username)

  return (
    <>
      <div className="poker-table">
        <div>{totalPot}</div>
        <CommonCards visibleCommonCards={visibleCommonCards} />
        <SeatContainer mySeat={mySeat} seats={seats} />
      </div>

      {pots.map(pot => {
        return(
          <div key={pot}>
            <div>{pot.seats}</div>
            <div>{pot.amount}</div>
            <div>{pot.winners}</div>
            <div>{pot.isOpen ? 'open' : 'closed'}</div>
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
              actionOn={mySeat.isActionOn}
              callAmount={mySeat.callAmount}
              minRaiseTo={mySeat.raiseTo}
              maxRaiseTo={mySeat.chipsInBack + mySeat.chipsInFront} />
      }
    </>
  )
}
