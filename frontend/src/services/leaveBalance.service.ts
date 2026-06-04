import apiFetch from "@/lib/apiFetch";
import { LeaveBalance } from "@/types/leave/leaveBalance";

export async function getMyLeaveBalances(): Promise<LeaveBalance[]> {
  return apiFetch<LeaveBalance[]>("/api/leave-balances/me");
}

export async function getEmployeeLeaveBalances(employeeId: string): Promise<LeaveBalance[]> {
  return apiFetch<LeaveBalance[]>(`/api/leave-balances/${employeeId}`);
}
