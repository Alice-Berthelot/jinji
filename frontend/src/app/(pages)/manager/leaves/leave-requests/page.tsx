import LeaveRequestsList from "@/components/LeaveRequestsList";
import BackArrow from "@/components/ui/BackArrow";
import MainTitle from "@/components/ui/MainTitle";
import { getLeaveRequestsSummary } from "@/services/leaveRequest.service";
import { getLeaveValidation } from "@/services/hrPolicy.service";

export default async function ManagerLeaveRequestsPage() {
  const [leaveRequests, hrPolicy] = await Promise.all([
    getLeaveRequestsSummary(),
    getLeaveValidation(),
  ]);
  return (
    <>
      <BackArrow />
      <MainTitle title="Demandes d'absence de l'équipe" />
      <section className="m-auto lg:my-0 lg:mx-8 bg-[var(--color-block-white)] px-6 py-4 shadow-sm rounded-sm w-[95%] lg:min-h-screen">
        <div className="flex flex-col justify-center items-center gap-4 lg:gap-8 mb-6">
          <LeaveRequestsList
            leaveRequests={leaveRequests}
            role="MANAGER"
            detailBasePath="/manager/leaves/leave-requests"
            hrPolicy={hrPolicy}
          />
        </div>
      </section>
    </>
  );
}
