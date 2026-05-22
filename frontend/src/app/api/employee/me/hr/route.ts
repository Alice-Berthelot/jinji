"use server";

import { EmployeeFullName } from "@/types/employee/employee";
import { cookies } from "next/headers";

export async function getEmployeeFullname(
  employeeId: string
): Promise<EmployeeFullName> {
  const cookieStore = await cookies();
  const token = cookieStore.get("access_token")?.value;

  const res = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/api/employees/${employeeId}/fullname`,
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
    throw new Error("Error occuring while loading employee full name");
  }

  return res.json();
}
