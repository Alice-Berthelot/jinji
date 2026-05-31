import LeaveRequestForm from "@/components/forms/LeaveRequestForm";
import BackArrow from "@/components/ui/BackArrow";
import MainTitle from "@/components/ui/MainTitle";
import { getRequestableLeaveTypes } from "@/services/leaveType.service";

export default async function NewLeaveRequestPage() {
  const leaveTypes = await getRequestableLeaveTypes();
  return (
    <>
      <BackArrow />
      <MainTitle title="Demande d'absence" />
      <section className="m-auto lg:my-0 lg:mx-8 bg-[var(--color-block-white)] px-6 py-4 shadow-sm rounded-sm w-[95%] lg:min-h-screen">
        <LeaveRequestForm leaveTypes={leaveTypes}/>
      </section>
    </>
  );
}
