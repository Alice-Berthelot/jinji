"use client";

import { usePathname } from "next/navigation";
import LogoutButton from "../ui/LogoutButton";
import LogoHeader from "../ui/LogoHeader";
import MenuBurger from "../ui/MenuBurger";

export default function Header() {
  // const isMobile = useIsMobile();
  const pathname = usePathname();
  
  if (pathname === '/login') {
    return null;
  }

  return (
    <header className="fixed z-100 w-full bg-[var(--color-block-white)] shadow-xl flex items-center p-3 top-0 justify-between">
      <div className="flex flex-row items-center gap-2">
        <MenuBurger />
        <LogoHeader />
        <p className="hidden md:block tracking-wide">jinji</p>
      </div>
      <div className="flex flex-row items-center gap-6">
        {/* <NotificationIcon /> */}
        {/* {!isMobile && <ConnectedEmployee />} */}
        <LogoutButton />
      </div>
    </header>
  );
}
