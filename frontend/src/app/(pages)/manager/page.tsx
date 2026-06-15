import HomeCard from "@/components/ui/HomeCard";
import MainTitle from "@/components/ui/MainTitle";
import Subtitle from "@/components/ui/Subtitle";
import { managerLinks } from "@/config/nav";

export default async function ManagerHomePage() {

  const links = managerLinks.filter((link) => link.link !== "/manager");

  return (
    <>
      <MainTitle
        title="Espace Manager"
        marginTop="mt-24"
      />
      <section className="m-auto lg:my-0 lg:mx-8 bg-[var(--color-block-white)] px-6 py-4 shadow-sm rounded-sm w-[95%] min-h-screen">
        <Subtitle subtitle="Mon espace Manager" />
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
