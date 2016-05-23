import React from 'react';

export default (props) => {
  return (
    <div className={props.seat.name === props.loggedInUsername ? 'my-seat' : ''}
      data-action-on={props.seat.actionOn}
      data-still-in-hand={props.seat.stillInHand}
      data-position={props.seat.position}>
      <p>{props.seat.name}</p>
      <p>{props.seat.chipsInFront}</p>
      <p>{props.seat.chipsInBack}</p>
      {props.seat.button ? <img src='/resources/img/button.png' /> : null}
      {props.seat.smallBlind ? <img src='/resources/img/smallBlind.png' /> : null}
      {props.seat.bigBlind ? <img src='/resources/img/bigBlind.png' /> : null}
    </div>
  )
}
