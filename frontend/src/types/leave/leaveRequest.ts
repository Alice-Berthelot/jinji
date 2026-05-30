export type LeaveRequestStatus = "APPROVED" | "REJECTED" | "PENDING" | "CANCELLED";
export type LeaveRequestWorkflowStatus = "APPROVED" | "REJECTED" | "PENDING_MANAGER" | "PENDING_HR" | "PENDING" | "CANCELLED";
export type LeaveRequestReviewDecision = "APPROVED" | "REJECTED";
export type ReviewerRole = "HR" | "MANAGER";

// export interface MyLeaveRequests {
//   id: number;
//   start_date: string;
//   start_period: Period;
//   end_date: string;
//   end_period: Period;
//   status_label: string;
//   employee_comment: string;
//   created_at: string;
//   leave_type_label: string;
//   number_of_days: number;
//   comment?: string;
// }

export interface MyLeaveRequestsSummary {
  id: number;
  leaveTypeLabel: string;
  startDate: string;
  endDate: string;
  status: LeaveRequestStatus;
  statusLabel: string;
  createdAt: string;
  hasManagerReview: boolean;
  hasHrReview: boolean;
  numberOfDays: number;
}

export interface LeaveRequest {
  leaveRequestId: number;
  leaveTypeLabel: string;
  employeeFirstName: string;
  employeeSurname: string;
  createdAt: string;
  startDate: string;
  endDate: string;
  startPeriod: Period;
  endPeriod: Period;
  status: LeaveRequestStatus;
  statusLabel: string;
  workflowStatus: LeaveRequestWorkflowStatus;
  employeeComment: string;
  numberOfDays: number;
  reviews: LeaveRequestReview[];
}

export interface LeaveRequestsSummary {
  id: number;
  employeeSurname: string;
  employeeFirstName: string;
  leaveTypeLabel: string;
  startDate: string;
  endDate: string;
  status: LeaveRequestStatus;
  statusLabel: string;
  createdAt: string;
  hasManagerReview: boolean;
  hasHrReview: boolean;
  numberOfDays: number;
}

export interface LeaveRequestReview {
  id: number;
  reviewerRole: ReviewerRole;
  decision: LeaveRequestReviewDecision;
  comment: string;
  reviewedAt: string;
  reviewerId: number;
  reviewerFirstName: string;
  reviewerLastName: string;
}

// export interface LeaveRequestTable {
//     leave_request_id: string;
//     employee: Employee;
//     employee_full_name: string;
//     leave_type_label: string;
//     start_date: string;
//     start_period: Period;
//     end_date: string;
//     end_period: Period;
//     status_label: string;
//     employee_comment: string;
//     created_at: string;
//     number_of_days: number;
//     reviews: LeaveRequestReview[];
//     comment?: string;
//   }


export type CreateLeaveRequestReviewPayload = {
  decision: "APPROVED" | "REJECTED";
  comment: string;
};