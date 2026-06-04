"use server";

import { decodeJwt, jwtVerify } from "jose";
import { cookies } from "next/headers";

const SECRET = new TextEncoder().encode(process.env.JWT_SECRET!);

type JwtPayload = {
  sub: string;
  roles: Role[];
};

export async function getUserRoles(): Promise<Role[]> {
  const cookieStore = await cookies();

  const token = cookieStore.get("access_token")?.value;

  if (!token) {
    return [];
  }

  try {
    const { payload } = await jwtVerify(token, SECRET);

    return (payload.roles as Role[]) || [];
  } catch {
    return [];
  }
}

export async function hasRole(role: Role): Promise<boolean> {
  const roles = await getUserRoles();

  return roles.includes(role);
}

export async function getAccessToken() {
  const cookieStore = await cookies();
  const token = cookieStore.get("access_token")?.value;

  if (!token) return null;

  const payload = decodeJwt(token);

  const isExpired = payload.exp! * 1000 < Date.now();

  if (!isExpired) {
    return token;
  }

  const newToken = await refreshTokens();

  return newToken;
}


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

export async function refreshTokens() {
  const cookieStore = await cookies();

  const refreshToken = cookieStore.get("refresh_token")?.value;

  if (!refreshToken) return null;

  const res = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/api/auth/refresh`,
    {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ refreshToken }),
    }
  );

  if (!res.ok) return null;

  const data = await res.json();

  cookieStore.set("access_token", data.accessToken, {
    httpOnly: true,
    secure: true,
    sameSite: "lax",
    path: "/",
  });

  cookieStore.set("refresh_token", data.refreshToken, {
    httpOnly: true,
    secure: true,
    sameSite: "lax",
    path: "/",
  });

  return data.accessToken;
}
