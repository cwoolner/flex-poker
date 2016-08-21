import React from 'react';
import CardData from './CardData';

export default ({visibleCommonCards}) => {
  return (
    <div>
      {
        visibleCommonCards.map((card, index) => {
          return (
            <span key={index}>
              <img className={'common-card'} src={CardData[card.id]} />
            </span>
          )
        })
      }
    </div>
  )
}
