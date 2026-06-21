import { test, expect } from '@playwright/test'
import { loginAs } from './helpers/auth'
import { createGame, joinGame, openJoinDialogForGame } from './helpers/game'

test.describe('lobby', () => {
  test('player can create a game and it appears in the list', async ({ page }) => {
    const gameName = `E2E Create ${Date.now()}`
    await loginAs(page)

    await createGame(page, gameName)

    // Verify details from the row (game list uses plain text tds)
    await expect(page.getByText(gameName)).toBeVisible()
    // Stage and counts are visible in the same row area
    const row = page.locator('tr', { hasText: gameName })
    await expect(row.getByText('REGISTERING')).toBeVisible()
    // Registered players column (3rd td, 0-based nth(2)) starts at 0 before anyone joins
    await expect(row.locator('td').nth(2)).toHaveText('0')
  })

  test('player can open join dialog from game list row', async ({ page }) => {
    const gameName = `E2E JoinDialog ${Date.now()}`
    await loginAs(page)
    await createGame(page, gameName)

    await openJoinDialogForGame(page, gameName)

    await expect(page.getByRole('dialog')).toBeVisible()
    await expect(page.locator('.modal-title', { hasText: 'Join Game' })).toBeVisible()
    await expect(page.getByLabel('Current Balance')).toBeVisible()
    await expect(page.getByRole('button', { name: 'Join Game' })).toBeVisible()
  })

  test('player can join a non-full game and land on the game page', async ({ page }) => {
    const gameName = `E2E JoinGame ${Date.now()}`
    await loginAs(page)
    await createGame(page, gameName)

    await joinGame(page, gameName)

    // Join triggers redirectToGame for non-completing joins
    await page.waitForURL(/\/game\/[^/]+$/)
    await expect(page.getByText('Game page')).toBeVisible()
  })
})
