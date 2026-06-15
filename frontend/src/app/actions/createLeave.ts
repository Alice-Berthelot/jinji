"use server";

import apiFetch from "@/lib/apiFetch";

export type LeaveState = {
  error: string | null;
  success?: boolean;
};

export async function createLeaveAction(
  prevState: LeaveState,
  formData: FormData
): Promise<LeaveState> {
  const payload = {
    employeeId: Number(formData.get("employeeId")),
    startDate: formData.get("startDate"),
    endDate: formData.get("endDate"),
    startPeriod: formData.get("startPeriod"),
    endPeriod: formData.get("endPeriod"),
    leaveTypeCode: formData.get("leaveTypeCode"),
  };

  try {
    await apiFetch("/api/leaves", {
      method: "POST",
      body: JSON.stringify(payload),
    });

    return {
      error: null,
      success: true,
    };
  } catch (e: unknown) {
    let message = "Erreur lors de la création de l'absence";
  
    if (e instanceof Error) {
      message = e.message;
    }
  
    return {
      error: message,
      success: false,
    };
  }
}