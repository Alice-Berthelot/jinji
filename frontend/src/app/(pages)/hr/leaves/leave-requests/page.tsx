import LeaveRequestsListWithLoadMore from "@/components/LeaveRequestsListWithLoadMore";
import BackArrow from "@/components/ui/BackArrow";
import MainTitle from "@/components/ui/MainTitle";
import { getLeaveValidation } from "@/services/hrPolicy.service";
import { getLeaveRequestsSummary } from "@/services/leaveRequest.service";

export default async function HrLeaveRequestsPage() {
  const [leaveRequests, hrPolicy] = await Promise.all([
    getLeaveRequestsSummary(0, 10),
    getLeaveValidation(),
  ]);

  return (
    <>
      <BackArrow />

      <MainTitle title="Demandes d'absence" />

      <section className="m-auto lg:my-0 lg:mx-8 bg-[var(--color-block-white)] px-6 py-4 shadow-sm rounded-sm w-[95%] min-h-screen">
        <div className="flex flex-col justify-center items-center gap-4 lg:gap-8 mb-6">
          <LeaveRequestsListWithLoadMore
            initialPage={leaveRequests}
            role="HR"
            detailBasePath="/hr/leaves/leave-requests"
            hrPolicy={hrPolicy}
          />
        </div>
      </section>
    </>
  );
}