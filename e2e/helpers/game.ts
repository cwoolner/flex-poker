import { Page, expect } from '@playwright/test'

export interface CreateGameOptions {
  players?: string
  playersPerTable?: string
  minutesBetweenBlindLevels?: string
  secondsForActionOnTimer?: string
}

export async function createGame(
  page: Page,
  gameName: string,
  options: CreateGameOptions = {},
) {
  const {
    players = '2',
    playersPerTable = '2',
    minutesBetweenBlindLevels = '1',
    secondsForActionOnTimer = '5',
  } = options

  await page.getByRole('button', { name: '+' }).click()

  await expect(page.getByRole('dialog')).toBeVisible()
  await expect(page.locator('.modal-title', { hasText: 'Create Game' })).toBeVisible()

  await page.getByLabel('Name').fill(gameName)
  await page.getByLabel('Number of Players (2 - 90)').fill(players)
  await page.getByLabel('Number of Players per Table (2 - 9)').fill(playersPerTable)
  await page.getByLabel('Blind increment in minutes (1 - 60)').fill(minutesBetweenBlindLevels)
  await page.getByLabel('Blind timer in seconds (1 - 60)').fill(secondsForActionOnTimer)

  await page.getByRole('button', { name: 'Create Game' }).click()

  // Modal closes (via state) and game list updates via WS push
  await expect(page.getByText(gameName)).toBeVisible()
  // ensure dialog fully dismissed so it doesn't intercept later clicks
  await expect(page.getByRole('dialog')).not.toBeVisible({ timeout: 5000 }).catch(() => {})
}

export async function openJoinDialogForGame(page: Page, gameName: string) {
  const row = page.locator('tr', { hasText: gameName })
  await row.getByRole('button', { name: 'Open' }).click()

  await expect(page.getByRole('dialog')).toBeVisible()
  await expect(page.locator('.modal-title', { hasText: 'Join Game' })).toBeVisible()
}

export async function joinGame(page: Page, gameName?: string) {
  if (gameName) {
    await openJoinDialogForGame(page, gameName)
  }
  await page.getByRole('button', { name: 'Join Game' }).click()
}
