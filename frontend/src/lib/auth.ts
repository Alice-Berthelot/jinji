"use server";

import { cookies } from "next/headers";
import { jwtVerify } from "jose";

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
