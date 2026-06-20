import fs from 'node:fs'
import path from 'node:path'
import { defineConfig, devices } from '@playwright/test'

const browsersPath = process.env.PLAYWRIGHT_BROWSERS_PATH
  ?? path.join(process.cwd(), '.playwright-browsers')

const chromiumExecutable = path.join(
  browsersPath,
  'chromium-1169/chrome-linux/chrome',
)

const launchOptions = fs.existsSync(chromiumExecutable)
  ? { executablePath: chromiumExecutable }
  : {}

const port = 8080
const baseURL = `http://localhost:${port}`

export default defineConfig({
  testDir: './e2e',
  fullyParallel: true,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 1 : 0,
  workers: process.env.CI ? 1 : undefined,
  reporter: [['list'], ['html', { open: 'never', outputFolder: 'playwright-report' }]],
  outputDir: 'test-results',
  use: {
    baseURL,
    trace: 'on-first-retry',
    screenshot: 'only-on-failure',
    launchOptions,
  },
  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] },
    },
  ],
  webServer: {
    command: 'npm run e2e:server',
    url: `${baseURL}/login`,
    reuseExistingServer: !process.env.CI,
    timeout: 180_000,
  },
})