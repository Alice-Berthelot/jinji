import apiFetch from "@/lib/apiFetch";
import {
  LeaveRequest,
  LeaveRequestsSummary,
  MyLeaveRequestsSummary,
  CreateLeaveRequestReviewPayload,
} from "@/types/leave/leaveRequest";
import { PageResponse } from "@/types/pagination/page";

export async function getLeaveRequestDetail(
  leaveRequestId: string
): Promise<LeaveRequest> {
  return apiFetch<LeaveRequest>(`/api/leave-requests/${leaveRequestId}`);
}

export async function getMyLeaveRequestsSummary(
  page: number,
  size: number = 5
): Promise<PageResponse<MyLeaveRequestsSummary>> {
  return apiFetch(
    `/api/leave-requests/me/summary?page=${page}&size=${size}`
  );
}

export async function getLeaveRequestsSummary(
  page: number,
  size: number = 10
): Promise<PageResponse<LeaveRequestsSummary>> {
  return apiFetch(
    `/api/leave-requests/summary?page=${page}&size=${size}`
  );
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

export async function cancelLeaveRequest(
  leaveRequestId: number
): Promise<void> {
  await apiFetch(
    `/api/leave-requests/${leaveRequestId}/cancel`,
    {
      method: "PATCH",
    }
  );
}