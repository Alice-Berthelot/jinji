import { defineConfig, devices } from "@playwright/test";

export default defineConfig({
  testDir: "./e2e",

  use: {
    baseURL: "http://localhost:3000",
  },

  projects: [
    {
      name: "setup-hr",
      testMatch: /auth\.setup\.ts/,
    },

    {
      name: "hr",
      use: {
        ...devices["Desktop Chrome"],
        storageState: "e2e/.auth/hr.json",
      },
      dependencies: ["setup-hr"],
    },
  ],

  webServer: {
    command: "npm run dev",
    url: "http://localhost:3000",
    reuseExistingServer: !process.env.CI,
  },
});