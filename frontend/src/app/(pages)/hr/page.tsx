import HomeCard from "@/components/ui/HomeCard";
import MainTitle from "@/components/ui/MainTitle";
import Subtitle from "@/components/ui/Subtitle";
import { hrLinks } from "@/config/nav";

export default async function HrHomePage() {
  const links = hrLinks.filter((link) => link.link !== "/hr");

  return (
    <>
      <MainTitle
        title="Espace RH"
        marginTop="mt-24"
      />
      <section className="m-auto lg:my-0 lg:mx-8 bg-[var(--color-block-white)] px-6 py-4 shadow-sm rounded-sm w-[95%] min-h-screen">
        <Subtitle subtitle="Mon espace RH" />
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
