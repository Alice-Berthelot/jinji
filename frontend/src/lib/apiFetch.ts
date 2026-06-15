"use server";

import { logout } from "@/app/actions/logout";
import { redirect } from "next/navigation";
import { deleteTokens, getAccessToken, refreshTokens } from "./auth";

async function apiFetch<T>(
  url: string,
  options: RequestInit = {},
  _retry = false
): Promise<T> {
  const token = await getAccessToken();

  if (!token) {
    await logout();
    throw new Error("NO_SESSION");
  }

  const res = await fetch(`${process.env.API_URL}${url}`, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      Authorization: token ? `Bearer ${token}` : "",
      ...(options.headers || {}),
    },
    cache: "no-store",
  });

  if (res.ok) {
    const contentType = res.headers.get("content-type");
  
    if (contentType?.includes("application/json")) {
      return res.json();
    }
  
    return undefined as T;
  }

  if (res.status === 403) {
    redirect("/?error=forbidden");
  }

  if (res.status === 401 && !_retry) {
    const newToken = await refreshTokens();

    if (!newToken) {
      await logout(); 
    }

    const retryRes = await fetch(
      `${process.env.API_URL}${url}`,
      {
        ...options,
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${newToken}`,
          ...(options.headers || {}),
        },
        cache: "no-store",
      }
    );

    if (retryRes.ok) return retryRes.json();

    await deleteTokens();
    throw new Error("SESSION_EXPIRED");
  }

  const errorData = await res.json().catch(() => null);

  throw new Error(
    errorData?.message ||
      errorData?.error ||
      `HTTP error ${res.status}`
  );
}

export default apiFetch;