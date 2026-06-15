import apiFetch from "@/lib/apiFetch";
import { LeaveValidation } from "@/types/leave/hrPolicy";

export async function getLeaveValidation(): Promise<LeaveValidation> {
  return apiFetch<LeaveValidation>(
    "/api/hr-policy/leave-validation"
  );
}

export async function updateLeaveValidation(
  leaveValidation: LeaveValidation
): Promise<void> {
  await apiFetch("/api/hr-policy/leave-validation", {
    method: "PATCH",
    body: JSON.stringify({
      leaveValidation,
    }),
  });
}