import React from 'react'
import CardData from './CardData'

export default ({myLeftCardId, myRightCardId}) => {
  return (
    <div>
      <img className={"my-cards"} src={CardData[myLeftCardId]} />
      <img className={"my-cards"} src={CardData[myRightCardId]} />
    </div>
  )
}
