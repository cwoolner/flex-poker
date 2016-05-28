import React from 'react';
import cardData from './cardData';

export default ({visibleCommonCards}) => {
  return (
    <div>
      {
        visibleCommonCards.map((card, index) => {
          return (
            <span key={index}>
              <img className={'common-card'} src={cardData[card.id]} />
            </span>
          )
        })
      }
    </div>
  )
}
