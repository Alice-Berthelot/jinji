import MainTitle from "@/components/ui/MainTitle";
import { getMyFullName } from "@/services/employee.service";

export default async function Home() {
  const employeeName = await getMyFullName();
  return (
    <>
      <MainTitle title={`Bonjour ${employeeName ? employeeName.firstName : ""}`} marginTop="mt-24" />
      <section className="m-auto lg:my-0 lg:mx-8 bg-[var(--color-block-white)] px-6 py-4 shadow-sm rounded-sm w-[95%] lg:min-h-screen">
        <p>En développement</p>
      </section>
    </>
  );
}
