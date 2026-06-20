import { describe, expect, it } from 'vitest'
import { render } from '@testing-library/react'
import CommonCards from './CommonCards'
import CardData from './CardData'

describe('CommonCards', () => {
  it('renders an image for each visible common card', () => {
    const visibleCommonCards = [{ id: 0 }, { id: 13 }, { id: 26 }]

    const { container } = render(<CommonCards visibleCommonCards={visibleCommonCards} />)
    const images = container.querySelectorAll('img.common-card')

    expect(images).toHaveLength(3)
    expect(images[0]).toHaveAttribute('src', CardData[0])
    expect(images[1]).toHaveAttribute('src', CardData[13])
    expect(images[2]).toHaveAttribute('src', CardData[26])
  })

  it('renders nothing when there are no visible common cards', () => {
    const { container } = render(<CommonCards visibleCommonCards={[]} />)

    expect(container.querySelectorAll('img.common-card')).toHaveLength(0)
  })
})