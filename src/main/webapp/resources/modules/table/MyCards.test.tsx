import { describe, expect, it } from 'vitest'
import { render } from '@testing-library/react'
import MyCards from './MyCards'
import CardData from './CardData'

describe('MyCards', () => {
  it('renders the correct card images for the given card ids', () => {
    const { container } = render(<MyCards myLeftCardId={0} myRightCardId={13} />)
    const images = container.querySelectorAll('img.my-cards')

    expect(images).toHaveLength(2)
    expect(images[0]).toHaveAttribute('src', CardData[0])
    expect(images[1]).toHaveAttribute('src', CardData[13])
  })
})