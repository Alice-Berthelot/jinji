"use client";

import { getMe } from "@/app/api/employee/me/route";
import ProfileInfo from "@/components/ProfileInfo";
import BackArrow from "@/components/ui/BackArrow";
import MainTitle from "@/components/ui/MainTitle";
import { EmployeeProfile } from "@/types/employee/employee";
import { useEffect, useState } from "react";

export default function Profile() {
  const [profile, setProfile] = useState<EmployeeProfile | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function load() {
      try {
        const data = await getMe();
        setProfile(data);
      } catch (err) {
        console.error(err);
      }
    }
    load();
  }, []);

  return (
    <>
      <BackArrow />
      <MainTitle title="Mon profil" />
      <section className="m-auto lg:my-0 lg:mx-8 bg-[var(--color-block-white)] px-6 py-4 shadow-sm rounded-sm w-[95%] lg:min-h-screen">
        {profile && <ProfileInfo profile={profile} />}
      </section>
    </>
  );
}
