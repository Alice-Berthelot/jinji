"use client";

import { usePathname } from "next/navigation";
import LogoutButton from "../ui/LogoutButton";
import LogoHeader from "../ui/LogoHeader";
import MenuBurger from "../ui/MenuBurger";
import { useIsMobile } from "@/hooks/useIsMobile";
import { useEffect, useState } from "react";
import { getMyFullName } from "@/app/api/employee/me/route";
import { EmployeeFullName } from "@/types/employee/employee";
import AuthenticatedEmployee from "../AuthenticatedEmployee";

export default function Header() {
  const isMobile = useIsMobile();
  const pathname = usePathname();
  const [myFullName, setMyFullName] = useState<EmployeeFullName | null>(null);

  if (pathname === "/login") {
    return null;
  }

  useEffect(() => {
    async function load() {
      try {
        const data = await getMyFullName();
        setMyFullName(data);
      } catch (err) {
        console.error(err);
      }
    }
    load();
  }, []);

  return (
    <header className="fixed z-100 w-full bg-[var(--color-block-white)] shadow-xl flex items-center p-3 top-0 justify-between">
      <div className="flex flex-row items-center gap-2">
        <MenuBurger />
        <LogoHeader />
        <p className="hidden md:block tracking-wide">jinji</p>
      </div>
      <div className="flex flex-row items-center gap-6">
        {/* <NotificationIcon /> */}
        {!isMobile && <AuthenticatedEmployee employeeFullName={myFullName} />}
        <LogoutButton />
      </div>
    </header>
  );
}
