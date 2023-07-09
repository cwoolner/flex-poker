import { Container } from 'react-bootstrap'
import Navigation from './modules/home/Navigation'
import MainTabs from './modules/home/MainTabs'
import { useSelector } from 'react-redux'
import { RootState } from '.'

export default () => {
  const username = useSelector((state: RootState) => state.userInfo.username)

  return (
    <>
      <Navigation username={username} />
      <Container>
        <MainTabs />
      </Container>
    </>
  )
}
