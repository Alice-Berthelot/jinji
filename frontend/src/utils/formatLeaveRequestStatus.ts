import { LeaveRequestStatus } from "@/types/leave/leaveRequest";


const leaveRequestStatusFrenchLabels: Record<LeaveRequestStatus, string> = {
  APPROVED: "Acceptée",
  REJECTED: "Refusée",
  PENDING: "En attente de validation",
  CANCELLED: "Annulée",
};

export function formatLeaveRequestStatus(status: LeaveRequestStatus) {
  return leaveRequestStatusFrenchLabels[status] ?? status;
}