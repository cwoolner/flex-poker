import { describe, expect, it } from 'vitest'
import { render, screen } from '@testing-library/react'
import ActionOnTimer from './ActionOnTimer'

describe('ActionOnTimer', () => {
  it('renders the action-on tick value', () => {
    render(<ActionOnTimer actionOnTick={12} />)

    expect(screen.getByText('12')).toBeInTheDocument()
  })
})