import { test, expect } from '@playwright/test'
import { loginAs } from './helpers/auth'
import { createGame, joinGame } from './helpers/game'

test.describe('table', () => {
  test('two players can create and join a minimal game and both reach a table', async ({ browser }) => {
    const gameName = `E2E Table ${Date.now()}`

    // Player 1 context and page
    const context1 = await browser.newContext()
    const page1 = await context1.newPage()
    await loginAs(page1, 'player1')
    await createGame(page1, gameName, { players: '2', playersPerTable: '2' })
    await joinGame(page1, gameName)

    // Player 2 context and page (separate session)
    const context2 = await browser.newContext()
    const page2 = await context2.newPage()
    await page2.goto('/login')
    await expect(page2.getByLabel('Username')).toBeVisible()
    await page2.getByLabel('Username').fill('player2')
    await page2.getByLabel('Password').fill('player2')
    await page2.getByRole('button', { name: 'Log In' }).click()
    await page2.waitForURL('/', { timeout: 10000 })
    await expect(page2.locator('.game-list')).toBeVisible()
    await joinGame(page2, gameName)

    // Both should end up at a table URL (last join completes the game and triggers openTable -> redirectToTable)
    await Promise.all([
      page1.waitForURL(/\/table\//, { timeout: 15000 }),
      page2.waitForURL(/\/table\//, { timeout: 15000 }),
    ])

    // Basic table UI checks (poker-table container + player seat names are rendered)
    await expect(page1.locator('.poker-table')).toBeVisible({ timeout: 10000 })
    await expect(page2.locator('.poker-table')).toBeVisible({ timeout: 10000 })

    // Seats render player names (scope to seat-holder to avoid nav dropdown match)
    await expect(page1.locator('.seat-holder').getByText('player1')).toBeVisible()
    await expect(page1.locator('.seat-holder').getByText('player2')).toBeVisible()
    await expect(page2.locator('.seat-holder').getByText('player1')).toBeVisible()
    await expect(page2.locator('.seat-holder').getByText('player2')).toBeVisible()

    await context1.close()
    await context2.close()
  })
})
