import { LeaveType } from "./leaveTypes";

export type LeaveCalendar = {
  employeeId?: string;
  firstName?: string;
  surname?: string;
  leaveType: LeaveType;
  startDate: string;
  endDate: string;
  leaveId: number;
  status: string;
};
