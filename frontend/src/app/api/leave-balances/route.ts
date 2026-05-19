"use server";

import { LeaveBalance } from "@/types/leave/leaveBalance";
import { cookies } from "next/headers";

export async function getMyLeaveBalance(): Promise<LeaveBalance[]> {
  const cookieStore = await cookies();
  const token = cookieStore.get("access_token")?.value;

  const res = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/api/leave-balances/me`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      cache: "no-store",
    }
  );

  if (!res.ok) {
    throw new Error("Error occuring while loading my leave balance data");
  }

  console.log(res.json);

  return res.json();
}