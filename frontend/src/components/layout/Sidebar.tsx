"use client";

import { usePathname } from "next/navigation";
import NavBar from "../ui/NavBar";
import { employeeLinks, hrLinks, managerLinks } from "@/config/nav";
// import { useRoles } from "@/lib/hooks/useRoles";

export default function Sidebar() {
  const pathname = usePathname();
  // const roles = useRoles();
  // const isManager = roles.includes("MANAGER");
  // const isHR = roles.includes("HR");

  let userSpaceTitle = "Espace personnel";
  // let userSpaceSubtitles = [];
  let links = employeeLinks;

  // if (isManager) {
  //   userSpaceSubtitles.push({ label: "→ Espace Manager", path: "/manager" });
  // }

  // if (isHR) {
  //   userSpaceSubtitles.push({ label: "→ Espace RH", path: "/rh" });
  // }

  if (pathname === "/login") {
    return null;
  }

  // if (pathname.startsWith("/rh")) {
  //   userSpaceTitle = "Espace RH";
  //   links = hrLinks;

  //   userSpaceSubtitles = [];
  //   userSpaceSubtitles.push({ label: "→ Espace personnel", path: "/" });
  //   if (isManager) {
  //     userSpaceSubtitles.push({ label: "→ Espace Manager", path: "/manager" });
  //   }
  // }

  // if (pathname.startsWith("/manager")) {
  //   userSpaceTitle = "Espace Manager";
  //   links = managerLinks;

  //   userSpaceSubtitles = [];
  //   userSpaceSubtitles.push({ label: "→ Espace personnel", path: "/" });
  //   if (isHR) {
  //     userSpaceSubtitles.push({ label: "→ Espace RH", path: "/rh" });
  //   }
  // }

  // if (pathname === "/") {
  //   userSpaceSubtitles = [];
    // if (isManager)
    //   userSpaceSubtitles.push({ label: "→ Espace Manager", path: "/manager" });
    // if (isHR) userSpaceSubtitles.push({ label: "→ Espace RH", path: "/rh" });
  // }

  return (
    <section className="hidden lg:block bg-[var(--color-sidebar-bg)] text-[var(--color-block-white)] w-64 min-h-screen mt-10 pl-4">
      <NavBar
        links={links}
        userSpaceTitle={userSpaceTitle}
        // userSpaceSubtitles={userSpaceSubtitles}
      />
    </section>
  );
}
