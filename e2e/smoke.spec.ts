import { test, expect } from '@playwright/test'
import { loginAs } from './helpers/auth'

test.describe('smoke', () => {
  test('login page loads', async ({ page }) => {
    await page.goto('/login')

    await expect(page.getByRole('heading', { name: 'Log in to Flex Poker' })).toBeVisible()
    await expect(page.getByLabel('Username')).toBeVisible()
    await expect(page.getByLabel('Password')).toBeVisible()
    await expect(page.getByRole('button', { name: 'Log In' })).toBeVisible()
  })

  test('player can log in and reach the lobby', async ({ page }) => {
    await loginAs(page)

    await expect(page.getByRole('button', { name: 'player1' })).toBeVisible()
    await expect(page.getByText('No games to show')).toBeVisible()
  })

  test('player can open the create game dialog', async ({ page }) => {
    await loginAs(page)

    await page.getByRole('button', { name: '+' }).click()

    await expect(page.getByRole('dialog')).toBeVisible()
    await expect(page.locator('.modal-title', { hasText: 'Create Game' })).toBeVisible()
    await expect(page.locator('input[name="name"]')).toBeVisible()
  })
})