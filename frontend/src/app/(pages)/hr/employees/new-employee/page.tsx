import NewEmployeeForm from "@/components/forms/NewEmployeeForm";
import BackArrow from "@/components/ui/BackArrow";
import MainTitle from "@/components/ui/MainTitle";
import { getDepartments } from "@/services/department.service";

export default async function NewEmployee() {
  const departments = await getDepartments();

  return (
    <>
      <BackArrow />
      <MainTitle title="Ajouter un collaborateur" />

      <section className="m-auto lg:my-0 lg:mx-8 bg-[var(--color-block-white)] px-6 py-4 shadow-sm rounded-sm w-[95%] lg:min-h-screen">
        <NewEmployeeForm departments={departments} />
      </section>
    </>
  );
}