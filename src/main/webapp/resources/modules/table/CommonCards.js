import React from 'react';
import cardData from './cardData';

export default (props) => {
  return (
    <div {...props}>
      {
        props.visibleCommonCards.map((card, index) => {
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
