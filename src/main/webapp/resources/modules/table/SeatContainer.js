import React from 'react'
import { connect } from 'react-redux'
import { Map } from 'immutable'
import Seat from './Seat'

const SeatContainer = ({ seats, mySeat, actionOnTick }) => {
  return (
    <div className={"seat-holder"}>
    {
      seats.map((seat, index) => <Seat seat={seat} mySeat={seat === mySeat} key={index} actionOnTick={actionOnTick} />)
    }
    </div>
  )
}

const mapStateToProps = state => ({ actionOnTick: state.actionOnTicks.get(state.activeTable.gameId, Map()).get(state.activeTable.tableId) })

export default connect(mapStateToProps)(SeatContainer)
