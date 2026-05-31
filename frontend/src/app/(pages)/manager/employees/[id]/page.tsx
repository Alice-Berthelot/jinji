import ProfileInfo from "@/components/ProfileInfo";
import BackArrow from "@/components/ui/BackArrow";
import LinkCustom from "@/components/ui/LinkCustom";
import MainTitle from "@/components/ui/MainTitle";
import { getEmployeeById } from "@/services/employee.service";

export default async function ManagerEmployeeProfilePage({
  params,
}: {
  params: { id: string };
}) {
  const { id } = await params;
  const employee = await getEmployeeById(Number(id), "MANAGER");

  return (
    <>
      <BackArrow />
      <MainTitle title="Fiche collaborateur" />
      <section className="m-auto lg:my-0 lg:mx-8 bg-[var(--color-block-white)] px-6 py-4 shadow-sm rounded-sm w-[95%] lg:min-h-screen">
        {employee && <ProfileInfo profile={employee} />}
      </section>
    </>
  );
}
