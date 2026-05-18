"use server"

import { LeaveValidation } from "@/types/leave/hrPolicy";
import { cookies } from "next/headers";

  
  export async function getLeaveValidation(): Promise<LeaveValidation> {
    const cookieStore = await cookies();
    const token = cookieStore.get("access_token")?.value;
  
    const res = await fetch(
      `${process.env.NEXT_PUBLIC_API_URL}/api/hr-policy/leave-validation`,
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