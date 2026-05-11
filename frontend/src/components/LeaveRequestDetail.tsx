import { LeaveRequest } from "@/types/leave/leaveRequest";

type LeaveRequestDetailProps = {
    leaveRequest: LeaveRequest | null;
    loading: boolean;
  };
  
  export default function LeaveRequestDetail({
    leaveRequest,
    loading,
  }: LeaveRequestDetailProps) {
    return (
        <>
        <p>{leaveRequest?.startDate}</p>
        <p>{leaveRequest?.endDate}</p>
        </>
    )
  }