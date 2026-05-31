import ProfileInfo from "@/components/ProfileInfo";
import BackArrow from "@/components/ui/BackArrow";
import LinkCustom from "@/components/ui/LinkCustom";
import MainTitle from "@/components/ui/MainTitle";
import { getEmployeeById } from "@/services/employee.service";
// import { getMe } from "@/services/employee.service";

export default async function HrEmployeeProfilePage({
  params,
}: {
  params: { id: string };
}) {
  const { id } = await params;
  const employee = await getEmployeeById(Number(id), "HR");

  return (
    <>
      <BackArrow />
      <div className="flex flex-col lg:flex-row lg:justify-between">
        <MainTitle title="Fiche collaborateur" />
        <LinkCustom
          title="Saisir une absence"
          href={`/hr/employees/${id}/new-leave`}
          className="self-center mb-10 lg:mt-24 lg:mr-16 w-56"
        />
      </div>
      <section className="m-auto lg:my-0 lg:mx-8 bg-[var(--color-block-white)] px-6 py-4 shadow-sm rounded-sm w-[95%] lg:min-h-screen">
        {employee && <ProfileInfo profile={employee} subtitle="Informations personnelles"/>}
      </section>
    </>
  );
}
