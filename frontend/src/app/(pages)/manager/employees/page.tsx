import ManagerEmployeesTable from "@/components/tables/ManagerEmployeesTable";
import BackArrow from "@/components/ui/BackArrow";
import MainTitle from "@/components/ui/MainTitle";
import { getManagerEmployees } from "@/services/employee.service";

export default async function ManagerEmployeesPage({
  searchParams,
}: {
  searchParams: Promise<{ page?: string; search?: string }>;
}) {
  const params = await searchParams;

  const page = Number(params.page ?? 0);

  const data = await getManagerEmployees(page, 10);

  return (
    <>
      <BackArrow />
      <div className="flex flex-col lg:flex-row lg:justify-between">
        <MainTitle title="Collaborateurs" />
      </div>
      <div className="flex flex-col lg:flex-row lg:justify-between">
        <section className="m-auto lg:my-0 lg:mx-8 bg-[var(--color-block-white)] px-6 py-4 shadow-sm rounded-sm w-[95%] min-h-screen">
          <ManagerEmployeesTable
            employees={data.content}
            page={data.number}
            totalPages={data.totalPages}
            hasNext={!data.last}
            hasPrevious={!data.first}
          />
        </section>
      </div>
    </>
  );
}
