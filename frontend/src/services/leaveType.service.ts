import apiFetch from "@/lib/apiFetch";
import { LeaveType } from "@/types/leave/leaveTypes";

export async function getLeaveTypes(): Promise<LeaveType[]> {
  return apiFetch<LeaveType[]>("/api/leave-types");
}