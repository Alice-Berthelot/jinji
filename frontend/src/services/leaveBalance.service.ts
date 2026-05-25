import apiFetch from "@/lib/apiFetch";
import { LeaveBalance } from "@/types/leave/leaveBalance";

export async function getMyLeaveBalance(): Promise<LeaveBalance[]> {
  return apiFetch<LeaveBalance[]>("/api/leave-balances/me");
}
