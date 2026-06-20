import { render, RenderOptions } from '@testing-library/react'
import { ReactElement, ReactNode } from 'react'
import { Provider } from 'react-redux'
import { createStore, Store } from 'redux'
import { MemoryRouter } from 'react-router-dom'
import reducer from '../reducers'
import { AppState, INITITAL_APP_STATE } from '../reducers/types'

interface RenderWithProvidersOptions extends Omit<RenderOptions, 'wrapper'> {
  preloadedState?: Partial<AppState>
  route?: string
}

export const renderWithProviders = (
  ui: ReactElement,
  {
    preloadedState = {},
    route = '/',
    ...renderOptions
  }: RenderWithProvidersOptions = {},
) => {
  const store: Store = createStore(reducer, {
    ...INITITAL_APP_STATE,
    ...preloadedState,
  })

  const Wrapper = ({ children }: { children: ReactNode }) => (
    <Provider store={store}>
      <MemoryRouter initialEntries={[route]}>
        {children}
      </MemoryRouter>
    </Provider>
  )

  return {
    store,
    ...render(ui, { wrapper: Wrapper, ...renderOptions }),
  }
}