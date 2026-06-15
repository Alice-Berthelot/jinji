import MainTitle from "@/components/ui/MainTitle";
import HomeCard from "@/components/ui/HomeCard";
import { employeeLinks } from "@/config/nav";
import { getMyFullName } from "@/services/employee.service";
import Subtitle from "@/components/ui/Subtitle";

export default async function Home() {
  const employeeName = await getMyFullName();

  const links = employeeLinks.filter((link) => link.link !== "/");

  return (
    <>
      <MainTitle
        title={`Bonjour ${employeeName ? employeeName.firstName : ""}`}
        marginTop="mt-24"
      />
      <section className="m-auto lg:my-0 lg:mx-8 bg-[var(--color-block-white)] px-6 py-4 shadow-sm rounded-sm w-[95%] min-h-screen">
        <Subtitle subtitle="Mon espace personnel" />
        <p className="hidden md:block">
          Accédez directement à la section souhaitée à l'aide des liens
          ci-dessous.
        </p>
        <div className="flex flex-wrap justify-center gap-8 mt-18">
          {links.map((link) => (
            <HomeCard key={link.link} link={link} />
          ))}
        </div>
      </section>
    </>
  );
}
