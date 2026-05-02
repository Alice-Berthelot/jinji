"use client";

import { logout } from "@/app/actions/logout";
import { FiLogOut } from "react-icons/fi";

export default function LogoutButton() {
  const handleLogout = async () => {
    await logout();
  };

  return (
    <button
      type="button"
      onClick={handleLogout}
      className="cursor-pointer mr-2"
      aria-label="Se déconnecter"
    >
      <FiLogOut size={18} />
    </button>
  );
}
