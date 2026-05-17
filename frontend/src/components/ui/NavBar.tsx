"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import LogoutButton from "./LogoutButton";

type NavLink = {
  name: string;
  link: string;
};

type UserSpaceSubtitle = {
  label: string;
  path: string;
};

type NavBarProps = {
  links: NavLink[];
  userSpaceTitle: string;
  userSpaceSubtitles?: UserSpaceSubtitle[];
};

export default function NavBar({
  links,
  userSpaceTitle,
  userSpaceSubtitles,
}: NavBarProps) {
  const pathname = usePathname();

  return (
    <>
      <div className="flex flex-col"></div>
      <p className="mt-10 pl-2 text-md mb-4 font-semibold">{userSpaceTitle}</p>
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
      <div className="border-t border-gray-300 mt-6 my-10 w-56"></div>
      <nav className="lg:text-sm">
        <ul className="flex flex-col gap-4">
          {links.map((item) => {
            const isActive = pathname === item.link;

            return (
              <li
                key={item.link}
                className={`
                hover:text-md hover:font-bold hover:tracking-wide
                ${
                  isActive
                    ? "bg-[var(--color-block-purple)] text-[var(--color-main-font)] rounded-md px-3 py-2 w-52 hover:font-normal hover:text-sm hover:tracking-normal m-[-12]"
                    : ""
                }
              `}
              >
                <Link href={item.link}>{item.name}</Link>
              </li>
            );
          })}
        </ul>
      </nav>
      <div className="border-t border-gray-300 mt-10 mb-6 w-56"></div>
      <LogoutButton variant="text" />
    </>
  );
}
