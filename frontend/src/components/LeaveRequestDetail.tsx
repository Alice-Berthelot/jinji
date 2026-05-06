import { MyLeaveRequestDetail } from "@/types/leave/leaveRequest";

type LeaveRequestDetailProps = {
    leaveRequest: MyLeaveRequestDetail | null;
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