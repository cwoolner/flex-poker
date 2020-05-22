import React from 'react'
import ActionOnTimer from './ActionOnTimer'

export default ({mySeat, seat, actionOnTick}) => {
  return (
    <div className={mySeat ? 'my-seat' : ''}
      data-action-on={seat.actionOn}
      data-still-in-hand={seat.stillInHand}
      data-position={seat.position}>
      {seat.actionOn ? <ActionOnTimer actionOnTick={actionOnTick} /> : null}
      <p>{seat.name}</p>
      <p>{seat.chipsInFront}</p>
      <p>{seat.chipsInBack}</p>
      {seat.button ? <img src='/resources/img/button.png' /> : null}
      {seat.smallBlind ? <img src='/resources/img/smallBlind.png' /> : null}
      {seat.bigBlind ? <img src='/resources/img/bigBlind.png' /> : null}
    </div>
  )
}
