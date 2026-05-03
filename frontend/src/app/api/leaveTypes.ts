"use server";

import { cookies } from "next/headers";

export async function getLeaveTypes() {
  const cookieStore = await cookies();
  const token = cookieStore.get("access_token")?.value;

  const res = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/api/leave-types`,
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
      cache: "no-store",
    }
  );

  if (!res.ok) {
    throw new Error("Erreur chargement types de congés");
  }

  const data = await res.json();

  console.log("PARSED JSON:", data);

  return data;
}