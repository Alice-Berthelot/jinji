import LeaveCalendar from "@/components/calendar/LeaveCalendar";
import LeaveBalanceTable from "@/components/tables/LeaveBalanceTable";
import LeaveRequestSmallTable from "@/components/tables/LeaveRequestSmallTable";
import BackArrow from "@/components/ui/BackArrow";
import LinkCustom from "@/components/ui/LinkCustom";
import MainTitle from "@/components/ui/MainTitle";
import Subtitle from "@/components/ui/Subtitle";
import { getMyLeaves } from "@/services/leave.service";
import { getMyLeaveBalances } from "@/services/leaveBalance.service";
import { getMyLeaveRequestsSummary } from "@/services/leaveRequest.service";
import { buildLeaveMap } from "@/utils/formatLeaveMap";

export default async function LeavePage({
  searchParams,
}: {
  searchParams: Promise<{ page?: string }>;
}) {
  const params = await searchParams;
  const page = Number(params.page ?? 0);
  const [leaveRequests, leaveBalance, leaves] = await Promise.all([
    getMyLeaveRequestsSummary(page, 4),
    getMyLeaveBalances(),
    getMyLeaves()
  ]);

  return (
    <>
      <BackArrow />
      <div className="flex flex-col lg:flex-row lg:justify-between">
        <MainTitle title="Mes absences" />
        <LinkCustom
          title="Nouvelle demande"
          href="/leaves/new-leave-request/"
          className="self-center mb-10 lg:mt-24 lg:mr-16"
        />
      </div>
        <div className="flex flex-col lg:flex-row justify-center items-center lg:items-start gap-4 lg:gap-8 lg:h-96">
          <section className="bg-[var(--color-block-white)] px-2 py-6 shadow-sm rounded-sm w-[95%] lg:w-[45%] lg:h-full">
            <Subtitle subtitle="Mon solde de congés payés" />
            <LeaveBalanceTable leaveBalance={leaveBalance} />
          </section>
          <section className="bg-[var(--color-block-white)] px-2 py-6 shadow-sm rounded-sm w-[95%] lg:w-[45%] lg:h-full">
            <Subtitle subtitle="Mes demandes d'absence" />
            <LeaveRequestSmallTable leaveRequests={leaveRequests} />
          </section>
        </div>
        <section className="mx-auto lg:ml-16 mt-4 lg:mt-8 mb-4 bg-[var(--color-block-white)] px-2 py-6 shadow-sm rounded-sm w-[95%] lg:w-[92%]">
          <Subtitle subtitle="Mon planning" />
          <LeaveCalendar leaveMap={buildLeaveMap(leaves)}/>
        </section>
    </>
  );
}
