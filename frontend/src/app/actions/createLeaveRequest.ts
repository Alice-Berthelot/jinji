"use server";

import { cookies } from "next/headers";

export type LeaveState = {
  error: string | null;
  success?: boolean;
};

export async function createLeaveAction(
  prevState: LeaveState,
  formData: FormData
): Promise<LeaveState> {
  const cookieStore = await cookies();
  const token = cookieStore.get("access_token")?.value;

  const payload = {
    startDate: formData.get("startDate"),
    endDate: formData.get("endDate"),
    startPeriod: formData.get("startPeriod"),
    endPeriod: formData.get("endPeriod"),
    leaveTypeCode: formData.get("leaveTypeCode"),
    employeeComment: formData.get("employeeComment"),
  };

  const res = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/api/leave-requests`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(payload),
    }
  );

  if (!res.ok) {
    return { error: "Erreur lors de la création de la demande de congé" };
  }

  return { error: null, success: true };
}