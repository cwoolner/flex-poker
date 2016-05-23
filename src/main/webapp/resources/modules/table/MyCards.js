import React from 'react';

export default (props) => {
  return (
    <div>
      <img className={"my-cards"} src={props.myLeftCard} />
      <img className={"my-cards"} src={props.myRightCard} />
    </div>
  )
}
