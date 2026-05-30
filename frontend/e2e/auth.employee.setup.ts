import { test as setup, expect } from "@playwright/test";

setup("authenticate employee", async ({ page }) => {
  await page.goto("/login");

  await page.fill('input[name="username"]', "employee@test.com");
  await page.fill('input[name="password"]', "password");

  await page.click('button[type="submit"]');

  await page.context().storageState({
    path: "e2e/.auth/employee.json",
  });
});