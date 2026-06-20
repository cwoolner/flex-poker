import { Page, expect } from '@playwright/test'

export async function loginAs(
  page: Page,
  username = 'player1',
  password = 'player1',
) {
  await page.goto('/login')
  await page.getByLabel('Username').fill(username)
  await page.getByLabel('Password').fill(password)
  await page.getByRole('button', { name: 'Log In' }).click()
  await page.waitForURL('/')
  await expect(page.locator('.game-list')).toBeVisible()
}