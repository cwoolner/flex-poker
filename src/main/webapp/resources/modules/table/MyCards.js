import React from 'react';

export default ({myLeftCard, myRightCard}) => {
  return (
    <div>
      <img className={"my-cards"} src={myLeftCard} />
      <img className={"my-cards"} src={myRightCard} />
    </div>
  )
}
