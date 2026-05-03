"use server"

import { EmployeeProfile } from "@/types/employee/employee";
import { cookies } from "next/headers";

  
  export async function getMe(): Promise<EmployeeProfile> {
    const cookieStore = await cookies();
    const token = cookieStore.get("access_token")?.value;
  
    const res = await fetch(
      `${process.env.NEXT_PUBLIC_API_URL}/api/employees/me`,
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
      throw new Error("Error occuring while loading employee data");
    }

    console.log(res.json);
  
    return res.json();
  }