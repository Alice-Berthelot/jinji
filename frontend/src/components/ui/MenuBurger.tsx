"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { RiCloseLargeLine } from "react-icons/ri";
import { FiLogOut, FiMenu } from "react-icons/fi";
import { getUserRoles } from "@/lib/auth";
import { logout } from "@/app/actions/logout";
import { employeeLinks, hrLinks, managerLinks } from "@/config/nav";
import { useIsMobile } from "@/hooks/useIsMobile";

interface NavBarProps {
  isOpen?: boolean;
  onClose?: () => void;
}

export default function MenuBurger({
  isOpen: initialOpen = false,
  onClose,
}: NavBarProps) {
  const [isOpen, setIsOpen] = useState(initialOpen);
  const isMobile = useIsMobile();
  const pathname = usePathname();
  const [roles, setRoles] = useState<Role[]>([]);

  useEffect(() => {
    async function getRoles() {
      try {
        const data = await getUserRoles();
        setRoles(data);
      } catch (err) {
        console.error(err);
      }
    }
    getRoles();
  }, []);
  
  const isManager = roles.includes("MANAGER");
  const isHR = roles.includes("HR");

  let userSpaceTitle = "Espace personnel";
  let userSpaceSubtitles = [];
  let links = employeeLinks;

  if (pathname === "/login") {
    return null;
  }

  if (pathname.startsWith("/hr")) {
    userSpaceTitle = "Espace RH";
    links = hrLinks;

    userSpaceSubtitles = [];
    userSpaceSubtitles.push({ label: "→ Espace personnel", path: "/" });
    if (isManager) {
      userSpaceSubtitles.push({ label: "→ Espace Manager", path: "/manager" });
    }
  }

  if (pathname.startsWith("/manager")) {
    userSpaceTitle = "Espace Manager";
    links = managerLinks;

    userSpaceSubtitles = [];
    userSpaceSubtitles.push({ label: "→ Espace personnel", path: "/" });
    if (isHR) {
      userSpaceSubtitles.push({ label: "→ Espace RH", path: "/hr" });
    }
  }

  if (pathname === "/") {
    userSpaceSubtitles = [];
    if (isManager)
      userSpaceSubtitles.push({ label: "→ Espace Manager", path: "/manager" });
    if (isHR) userSpaceSubtitles.push({ label: "→ Espace RH", path: "/hr" });
  }

  const handleClose = () => {
    setIsOpen(false);
    if (onClose) onClose();
  };

  const handleClick = (
    e: React.MouseEvent<HTMLButtonElement | HTMLDivElement>
  ) => {
    e.preventDefault();
    setIsOpen(!isOpen);
  };

  const handleLogout = async () => {
    await logout();
  };

  if (!isMobile) return null;

  const classLi =
    "relative transform transition-all ease-in-out duration-300 hover:text-white p-2";

  return (
    <>
      <button
        onClick={handleClick}
        className="relative w-10 h-10 flex items-center justify-center text-ghostwhite text-2xl z-100 md:hidden"
      >
        <FiMenu
          className={`absolute transition-all duration-500 ease-in-out text-ghostwhite/80 ${
            isOpen ? "opacity-0 scale-0" : "opacity-100 scale-100"
          }`}
        />
        <RiCloseLargeLine
          className={`absolute transition-all duration-500 ease-in-out text-ghostwhite/80${
            isOpen ? "opacity-100 scale-100" : "opacity-0 scale-0"
          }`}
        />
      </button>
      <nav className="z-30">
        {isOpen && (
          <div
            className="fixed inset-0 bg-black/10 z-40 top-16"
            onClick={handleClose}
          />
        )}

        {isOpen && (
          <>
            <div className="fixed top-16 left-0 h-full w-80 bg-[var(--color-block-white)] shadow-lg z-50 p-6 flex flex-col gap-4 list-none">
              <p className="mt-10 pl-2 text-md mb-4">{userSpaceTitle}</p>
              <div className="flex flex-col">
                {userSpaceSubtitles?.map((subtitle, i) => (
                  <Link
                    key={i}
                    href={subtitle.path}
                    className="ml-4 hover:text-md hover:font-bold hover:tracking-wide"
                  >
                    {subtitle.label}
                  </Link>
                ))}
              </div>
              <div className="border-t border-gray-300 mt-10 mb-6 w-64"></div>
              {links.map((link, index) => (
                <li
                  key={index}
                  className={`${classLi} ${
                    pathname === link.link
                      ? "text-[var(--color-dark-purple)]"
                      : "text-[var(--color-main-font)]"
                  }`}
                >
                  <Link href={link.link} onClick={handleClose}>
                    {link.name}
                  </Link>
                </li>
              ))}
              <div className="border-t border-gray-300 mt-10 mb-6 w-64"></div>
              <button
                type="button"
                onClick={handleLogout}
                className="cursor-pointer underline underline-offset-2 flex gap-2 items-center hover:font-bold text-sm mt-10 ml-6 mb-6"
              >
                <span>Se déconnecter</span>
                <FiLogOut size={15} />
              </button>
            </div>
          </>
        )}
      </nav>
    </>
  );
}
