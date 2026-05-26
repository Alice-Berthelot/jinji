import LeaveRequestDetail from "@/components/LeaveRequestDetail";
import BackArrow from "@/components/ui/BackArrow";
import MainTitle from "@/components/ui/MainTitle";
import { getLeaveRequestDetail } from "@/services/leaveRequest.service";

export default async function LeaveRequestDetailPage({
  params,
}: {
  params: Promise<{ id: string }>;
}) {
  const { id } = await params;

  const leaveRequest = await getLeaveRequestDetail(id);

  return (
    <>
      <BackArrow />
      <MainTitle title={`Demande d'absence n°${id}`} />

      <section className="m-auto lg:my-0 lg:mx-8 bg-[var(--color-block-white)] px-6 py-4 shadow-sm rounded-sm w-[95%] lg:min-h-screen">
        <LeaveRequestDetail
          leaveRequest={leaveRequest}
          loading={false}
          userRole="HR"
        />
      </section>
    </>
  );
}