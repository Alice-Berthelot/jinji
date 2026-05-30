import NewEmployeeForm from "@/components/forms/NewEmployeeForm";
import BackArrow from "@/components/ui/BackArrow";
import LinkCustom from "@/components/ui/LinkCustom";
import MainTitle from "@/components/ui/MainTitle";
import { getDepartments } from "@/services/department.service";

export default async function HrEmployeesPage() {
  return (
    <>
      <BackArrow />
      <div className="flex flex-col lg:flex-row lg:justify-between">
      <MainTitle title="Gestion des collaborateurs" />
      <LinkCustom
          title="Ajouter un collaborateur"
          href="/hr/employees/new-employee/"
          className="self-center mb-10 lg:mt-24 lg:mr-16 w-56"
        />
      </div>
      <div className="flex flex-col lg:flex-row lg:justify-between">
      <section className="m-auto lg:my-0 lg:mx-8 bg-[var(--color-block-white)] px-6 py-4 shadow-sm rounded-sm w-[95%] lg:min-h-screen">
        <p>En développement</p>
      </section>
      </div>
    </>
  );
}
