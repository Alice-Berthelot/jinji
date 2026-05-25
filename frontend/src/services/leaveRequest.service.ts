import apiFetch from "@/lib/apiFetch";
import {
  LeaveRequest,
  LeaveRequestsSummary,
  MyLeaveRequestsSummary,
  CreateLeaveRequestReviewPayload,
} from "@/types/leave/leaveRequest";

export async function getLeaveRequestDetail(
  leaveRequestId: string
): Promise<LeaveRequest> {
  return apiFetch<LeaveRequest>(`/api/leave-requests/${leaveRequestId}`);
}

export async function getMyLeaveRequestsSummary(): Promise<
  MyLeaveRequestsSummary[]
> {
  return apiFetch<MyLeaveRequestsSummary[]>("/api/leave-requests/me/summary");
}

export async function getLeaveRequestsSummary(): Promise<
  LeaveRequestsSummary[]
> {
  return apiFetch<LeaveRequestsSummary[]>("/api/leave-requests/summary");
}

export async function createLeaveRequestReview(
  leaveRequestId: number,
  payload: CreateLeaveRequestReviewPayload
): Promise<LeaveRequest> {
  return apiFetch<LeaveRequest>(
    `/api/leave-requests/${leaveRequestId}/review`,
    {
      method: "POST",
      body: JSON.stringify(payload),
    }
  );
}
