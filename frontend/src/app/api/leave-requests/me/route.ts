"use server"

import { MyLeaveRequestsSummary } from "@/types/leave/leaveRequest";
import { cookies } from "next/headers";

  
  export async function getMyLeaveRequestsSummary(): Promise<MyLeaveRequestsSummary[]> {
    const cookieStore = await cookies();
    const token = cookieStore.get("access_token")?.value;
  
    const res = await fetch(
      `${process.env.NEXT_PUBLIC_API_URL}/api/leave-requests/me/summary`,
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
      throw new Error("Error occuring while loading leave request data");
    }

    return res.json();
  }