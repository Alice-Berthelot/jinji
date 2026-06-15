import EmployeeTable from "@/components/tables/EmployeesTable";
import BackArrow from "@/components/ui/BackArrow";
import LinkCustom from "@/components/ui/LinkCustom";
import MainTitle from "@/components/ui/MainTitle";
import SearchBar from "@/components/ui/SearchBar";
import { getEmployees } from "@/services/employee.service";

export default async function HrEmployeesPage({
  searchParams,
}: {
  searchParams: Promise<{ page?: string; search?: string }>;
}) {
  const params = await searchParams;

  const page = Number(params.page ?? 0);
  const search = params.search ?? "";

  const data = await getEmployees(page, 10, search);

  return (
    <>
      <BackArrow />
      <div className="flex flex-col lg:flex-row lg:justify-between">
        <MainTitle title="Collaborateurs" />
        <LinkCustom
          title="Ajouter un collaborateur"
          href="/hr/employees/new-employee/"
          className="self-center mb-10 lg:mt-24 lg:mr-16 w-56"
        />
      </div>
      <div className="flex flex-col lg:flex-row lg:justify-between">
        <section className="m-auto lg:my-0 lg:mx-8 bg-[var(--color-block-white)] px-6 py-4 shadow-sm rounded-sm w-[95%] min-h-screen">
          <SearchBar search={search} />
          <EmployeeTable
            employees={data.content}
            page={data.number}
            totalPages={data.totalPages}
            hasNext={!data.last}
            hasPrevious={!data.first}
            search={search}
          />
        </section>
      </div>
    </>
  );
}
