"use client";

import { logout } from "@/app/actions/logout";
import { FiLogOut } from "react-icons/fi";

type LogoutButtonProps = {
  variant?: "icon" | "text";
};

export default function LogoutButton({ variant = "icon" }: LogoutButtonProps) {
  const handleLogout = async () => {
    await logout();
  };

  return variant === "icon" ? (
    <button
      type="button"
      onClick={handleLogout}
      className="cursor-pointer mr-2 hover:text-[var(--color-dark-purple)]"
      aria-label="Se déconnecter"
    >
      <FiLogOut size={18} />
    </button>
  ) : (
    <button
      type="button"
      onClick={handleLogout}
      className="cursor-pointer underline underline-offset-2 flex gap-2 items-center hover:font-bold text-sm mt-10"
    >
      <span>Se déconnecter</span>
      <FiLogOut size={15} />
    </button>
  );
}
