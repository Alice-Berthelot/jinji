"use server";

import { cookies } from "next/headers";

export async function deleteTokens() {
  const cookieStore = await cookies();

  cookieStore.delete({
    name: "access_token",
    path: "/",
  });

  cookieStore.delete({
    name: "refresh_token",
    path: "/",
  });
}
