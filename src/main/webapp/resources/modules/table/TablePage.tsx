import { useEffect } from 'react';
import CommonCards from './CommonCards'
import MyCards from './MyCards'
import PokerActions from './PokerActions'
import SeatContainer from './SeatContainer'
import { useDispatch, useSelector } from 'react-redux'
import { changeChatMsgStream, changeTable } from '../../reducers'
import { useParams } from 'react-router-dom'
import { RootState } from '../..'
import { DEFAULT_GAME, DEFAULT_TABLE } from '../../reducers/types';

export default () => {

  const { gameId, tableId } = useParams()

  const { totalPot, visibleCommonCards, seats, pots, cardId1, cardId2, username } = useSelector((state: RootState) => {
    const tableData = state.tables.get(state.activeTable.gameId, DEFAULT_GAME).get(state.activeTable.tableId, DEFAULT_TABLE)
    const pocketCardData = state.pocketCards.get(tableData.currentHandId)
    return {
      ...tableData,
      ...pocketCardData,
      ...state.userInfo,
    }
  })

  const dispatch = useDispatch()

  useEffect(() => {
    dispatch(changeChatMsgStream(gameId, tableId))
  }, [gameId, tableId])

  useEffect(() => {
    dispatch(changeTable(gameId, tableId))
  }, [gameId, tableId])

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
          <div key={pot.toString()}>
            <div>{pot.seats}</div>
            <div>{pot.amount}</div>
            <div>{pot.winners}</div>
            <div>{pot.isOpen ? 'open' : 'closed'}</div>
          </div>
        )
      })}

      <MyCards myLeftCardId={cardId1} myRightCardId={cardId2} />
      {
        mySeat
          ? <PokerActions
              gameId={gameId}
              tableId={tableId}
              actionOn={mySeat.isActionOn}
              callAmount={mySeat.callAmount}
              minRaiseTo={mySeat.raiseTo}
              maxRaiseTo={mySeat.chipsInBack + mySeat.chipsInFront} />
          : null
      }
    </>
  )
}
