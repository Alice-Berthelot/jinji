import { LeaveType } from "./leaveTypes";

export interface MyLeaveRequests {
  id: number;
  start_date: string;
  start_period: Period;
  end_date: string;
  end_period: Period;
  status_label: string;
  employee_comment: string;
  created_at: string;
  leave_type_label: string;
  number_of_days: number;
  comment?: string;
}

export interface MyLeaveRequestsSummary {
  id: number;
  leaveTypeLabel: string;
  startDate: string;
  endDate: string;
  status: LeaveRequestStatus;
  createdAt: string;
}

export type LeaveRequestStatus = "ACCEPTED" | "REJECTED" | "PENDING" | "CANCELLED";

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

//   export interface LeaveRequestDetail {
//     id: string;
//     employee: Employee;
//     employee_full_name: string;
//     leave_type: LeaveType;
//     start_date: Period;
//     start_period: Period;
//     end_date: string;
//     end_period: string;
//     status: string;
//     status_label: string;
//     source: string;
//     paper_document?: string | null;
//     employee_comment: string;
//     created_at: string;
//     number_of_days: number;
//     reviews: LeaveRequestReview[];
//     comment?: string;
//   }
