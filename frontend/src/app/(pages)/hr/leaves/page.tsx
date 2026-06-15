import LeaveCalendar from "@/components/calendar/LeaveCalendar";
import BackArrow from "@/components/ui/BackArrow";
import LinkCustom from "@/components/ui/LinkCustom";
import MainTitle from "@/components/ui/MainTitle";
import Subtitle from "@/components/ui/Subtitle";
import { getLeaves } from "@/services/leave.service";
import { buildLeaveMap } from "@/utils/formatLeaveMap";

export default async function HrLeavePage() {
  const leaves = await getLeaves("HR");
  return (
    <>
      <BackArrow />
      <div className="flex flex-col lg:flex-row lg:justify-between">
        <MainTitle title="Absences" />
        <LinkCustom
          title="Demandes d'absence"
          href="/hr/leaves/leave-requests/"
          className="self-center mb-10 lg:mt-24 lg:mr-16"
        />
      </div>
      <section className="mx-auto lg:ml-16 mt-4 mb-4 bg-[var(--color-block-white)] px-2 py-6 shadow-sm rounded-sm w-[95%] lg:w-[92%]">
        <Subtitle subtitle="Planning des absences" />
        <LeaveCalendar leaveMap={buildLeaveMap(leaves)} />
      </section>
    </>
  );
}
