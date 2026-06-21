import { Page, expect } from '@playwright/test'

export async function loginAs(
  page: Page,
  username = 'player1',
  password = 'player1',
) {
  for (let attempt = 0; attempt < 2; attempt++) {
    await page.goto('/login')
    // ensure SPA/react form is ready (avoid race filling before inputs mounted)
    await expect(page.getByLabel('Username')).toBeVisible()
    await page.getByLabel('Username').fill(username)
    await page.getByLabel('Password').fill(password)
    await page.getByRole('button', { name: 'Log In' }).click()
    try {
      await page.waitForURL('/', { timeout: 8000 })
      break
    } catch {
      if (attempt === 1) throw new Error(`loginAs for ${username} failed after retries`)
      // brief pause before retry (transient timing / render / load)
      await page.waitForTimeout(200)
    }
  }
  await expect(page.locator('.game-list')).toBeVisible()
}