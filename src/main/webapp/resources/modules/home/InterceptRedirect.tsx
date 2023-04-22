import { useEffect } from 'react';
import { Navigate } from "react-router-dom";
import { useSelector, useDispatch } from 'react-redux'
import { clearRedirect } from '../../reducers';
import { RootState } from '../..';

export default ({ children }) => {
  const dispatch = useDispatch()
  const redirectUrl = useSelector((state: RootState) => state.redirectUrl)

  useEffect(() => {
    dispatch(clearRedirect())
  }, [redirectUrl])

  if (redirectUrl) {
    return <Navigate to={redirectUrl} />
  } else {
    return children
  }
}
