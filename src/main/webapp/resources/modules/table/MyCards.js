import React from 'react';

export default (props) => {
  return (
    <div>
      <img class="my-cards" src={props.myLeftCard} />
      <img class="my-cards" src={props.myRightCard} />
    </div>
  )
}
