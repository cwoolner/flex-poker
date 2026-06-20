import { defineConfig } from 'vitest/config'

export default defineConfig({
  test: {
    include: ['src/main/webapp/resources/**/*.test.ts'],
    environment: 'node',
  },
})