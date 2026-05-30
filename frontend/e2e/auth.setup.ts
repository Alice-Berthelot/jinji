import { test as setup, expect } from "@playwright/test";

const authFile = "e2e/.auth/hr.json";

setup("authenticate HR via API", async ({ request, page }) => {
  // 1. LOGIN VIA API (direct backend)
  const response = await request.post(
    "http://localhost:8080/api/auth/login",
    {
      data: {
        username: "hr@test.com",
        password: "password123456",
      },
    }
  );

  console.log(await response.text());

  expect(response.ok()).toBeTruthy();

  const data = await response.json();

  // 2. OUVRIR UNE PAGE POUR ATTACHER LE COOKIE AU CONTEXT BROWSER
  await page.goto("/");

  // 3. INJECTER COOKIE DANS LE BROWSER CONTEXT
  await page.context().addCookies([
    {
      name: "access_token",
      value: data.accessToken,
      domain: "localhost",
      path: "/",
      httpOnly: true,
      secure: false, // localhost
      sameSite: "Lax",
    },
    {
      name: "refresh_token",
      value: data.refreshToken,
      domain: "localhost",
      path: "/",
      httpOnly: true,
      secure: false,
      sameSite: "Lax",
    },
  ]);

  // 4. SAUVEGARDER STORAGE STATE
  await page.context().storageState({
    path: authFile,
  });
});