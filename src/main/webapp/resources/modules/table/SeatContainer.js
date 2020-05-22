import React from 'react'
import { useSelector } from 'react-redux'
import { Map } from 'immutable'
import Seat from './Seat'

export default ({ seats, mySeat }) => {
  const actionOnTick = useSelector(state => state.actionOnTicks.get(state.activeTable.gameId, Map()).get(state.activeTable.tableId))

  return (
    <div className={"seat-holder"}>
    {
      seats.map((seat, index) => <Seat seat={seat} mySeat={seat === mySeat} key={index} actionOnTick={actionOnTick} />)
    }
    </div>
  )
}
