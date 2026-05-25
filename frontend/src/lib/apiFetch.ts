"use server";

import { cookies } from "next/headers";

async function apiFetch<T>(
  url: string,
  options: RequestInit = {}
): Promise<T> {
  const cookieStore = await cookies();
  const token = cookieStore.get("access_token")?.value;

  const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}${url}`, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      Authorization: token ? `Bearer ${token}` : "",
      ...(options.headers || {}),
    },
    cache: "no-store",
  });

  if (!res.ok) {
    const errorData = await res.json().catch(() => null);

    console.error("API error:", errorData);

    throw new Error(
      errorData?.message ||
        errorData?.error ||
        `HTTP error ${res.status}`
    );
  }

  return res.json();
}

export default apiFetch;