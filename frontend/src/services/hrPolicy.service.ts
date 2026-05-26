import apiFetch from "@/lib/apiFetch";
import { LeaveValidation } from "@/types/leave/hrPolicy";

export async function getLeaveValidation(): Promise<LeaveValidation> {
  return apiFetch<LeaveValidation>(
    "/api/hr-policy/leave-validation"
  );
}