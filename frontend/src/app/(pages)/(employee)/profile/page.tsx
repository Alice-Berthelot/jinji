import ProfileInfo from "@/components/ProfileInfo";
import BackArrow from "@/components/ui/BackArrow";
import MainTitle from "@/components/ui/MainTitle";
import { getMe } from "@/services/employee.service";

export default async function ProfilePage() {
  const profile = await getMe();
  return (
    <>
      <BackArrow />
      <MainTitle title="Mon profil" />
      <section className="m-auto lg:my-0 lg:mx-8 bg-[var(--color-block-white)] px-6 py-4 shadow-sm rounded-sm w-[95%] lg:min-h-screen">
        {profile && <ProfileInfo profile={profile} subtitle="Mes informations personnelles"/>}
      </section>
    </>
  );
}
