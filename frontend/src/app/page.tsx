import BackArrow from "@/components/ui/BackArrow";
import MainTitle from "@/components/ui/MainTitle";
import { getMyFullName } from "./api/employee/me/route";

export default async function Home() {
  const employeeName = await getMyFullName();
  return (
    <>
      <BackArrow />
      <MainTitle title={`Bienvenue ${employeeName ? employeeName.firstName : ""}`} />
      <section className="m-auto lg:my-0 lg:mx-8 bg-[var(--color-block-white)] px-6 py-4 shadow-sm rounded-sm w-[95%] lg:min-h-screen">
        <p>En développement</p>
      </section>
    </>
  );
}
