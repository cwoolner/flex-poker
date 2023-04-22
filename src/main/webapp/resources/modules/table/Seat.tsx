import ActionOnTimer from './ActionOnTimer'

export default ({mySeat, seat, actionOnTick}) => {
  return (
    <div className={mySeat ? 'my-seat' : ''}
      data-action-on={seat.isActionOn}
      data-still-in-hand={seat.isStillInHand}
      data-position={seat.position}>
      {seat.isActionOn ? <ActionOnTimer actionOnTick={actionOnTick} /> : null}
      <p>{seat.name}</p>
      <p>{seat.chipsInFront}</p>
      <p>{seat.chipsInBack}</p>
      {seat.isButton ? <img src='/resources/img/button.png' /> : null}
      {seat.isSmallBlind ? <img src='/resources/img/smallBlind.png' /> : null}
      {seat.isBigBlind ? <img src='/resources/img/bigBlind.png' /> : null}
    </div>
  )
}
