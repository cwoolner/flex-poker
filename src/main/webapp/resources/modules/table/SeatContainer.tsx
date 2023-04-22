import { useSelector } from 'react-redux'
import { Map } from 'immutable'
import Seat from './Seat'
import { RootState } from '../..'

export default ({ seats, mySeat }) => {
  const actionOnTick = useSelector((state: RootState) =>
    state.actionOnTicks.get(state.activeTable.gameId, Map()).get(state.activeTable.tableId))

  return (
    <div className={"seat-holder"}>
    {
      seats.map((seat, index) => <Seat seat={seat} mySeat={seat === mySeat} key={index} actionOnTick={actionOnTick} />)
    }
    </div>
  )
}
