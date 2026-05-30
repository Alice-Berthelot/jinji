import { test, expect } from "@playwright/test";

test("HR peut accéder aux leave requests", async ({ page }) => {
  await page.goto("/hr/leave-requests");

  await expect(
    page.getByText("Demandes de congés")
  ).toBeVisible();
});