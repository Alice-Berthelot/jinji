import BackArrow from "@/components/ui/BackArrow";
import MainTitle from "@/components/ui/MainTitle";
import NewLeaveForm from "@/components/forms/NewLeaveForm";
import { getEmployeeFullname } from "@/app/api/employee/me/hr/route";

type HrNewLeavePageProps = {
  params: Promise<{
    id: string;
  }>;
};

export default async function HrNewLeavePage({ params }: HrNewLeavePageProps) {
  const { id } = await params;

  const employeeName = await getEmployeeFullname(id);
  const employeeFullName = employeeName.firstName + " " + employeeName.surname;

  return (
    <>
      <BackArrow />
      <MainTitle title="Saisir une absence" />
      <section className="m-auto lg:my-0 lg:mx-8 bg-[var(--color-block-white)] px-6 py-4 shadow-sm rounded-sm w-[95%] lg:min-h-screen">
        <NewLeaveForm
          employeeId={Number(id)}
          subtitle={`Saisir une absence pour le compte de ${employeeFullName}`}
        />
      </section>
    </>
  );
}
