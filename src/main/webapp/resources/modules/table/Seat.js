import React from 'react';

export default ({mySeat, seat}) => {
  return (
    <div className={mySeat ? 'my-seat' : ''}
      data-action-on={seat.actionOn}
      data-still-in-hand={seat.stillInHand}
      data-position={seat.position}>
      <p>{seat.name}</p>
      <p>{seat.chipsInFront}</p>
      <p>{seat.chipsInBack}</p>
      {seat.button ? <img src='/resources/img/button.png' /> : null}
      {seat.smallBlind ? <img src='/resources/img/smallBlind.png' /> : null}
      {seat.bigBlind ? <img src='/resources/img/bigBlind.png' /> : null}
    </div>
  )
}
