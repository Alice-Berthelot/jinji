import { LeaveType } from "./leaveTypes";

export interface LeaveBalance {
  id: number;
  label: string;
  acquisitionStartDate: string;
  acquisitionEndDate: string;
  acquiredDays: number;
  takenDays: number;
  remainingDays: number;
  leaveType: LeaveType;
}
