"use server";

import apiFetch from "@/lib/apiFetch";

export type LeaveState = {
  error: string | null;
  success?: boolean;
};

export async function createLeaveRequestAction(
  prevState: LeaveState,
  formData: FormData
): Promise<LeaveState> {
  try {
  const payload = {
    startDate: formData.get("startDate"),
    endDate: formData.get("endDate"),
    startPeriod: formData.get("startPeriod"),
    endPeriod: formData.get("endPeriod"),
    leaveTypeCode: formData.get("leaveTypeCode"),
    employeeComment: formData.get("employeeComment"),
  };

  await apiFetch("/api/leave-requests", {
    method: "POST",
    body: JSON.stringify(payload),
  });

  return {
    error: null,
    success: true,
  };
} catch (error) {
  return {
    error:
      error instanceof Error
        ? "Erreur lors de la création de la demande de congé"
        : "Erreur lors de la création de la demande de congé",
  };
}
}