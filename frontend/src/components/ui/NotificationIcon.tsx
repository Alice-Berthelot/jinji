"use client";

import Link from "next/link";
import { IoNotificationsSharp } from "react-icons/io5";
import { useUnreadNotifications } from "@/hooks/useUnreadNotifications";

export default function NotificationIcon() {
  const unreadCount = useUnreadNotifications();

  return (
    <Link
      href="/notifications"
      className="relative"
      aria-label={
        unreadCount > 0
          ? `Notifications, ${unreadCount} non lues`
          : "Notifications"
      }
    >
      <IoNotificationsSharp
        size={23}
        className={
          unreadCount > 0 ? "text-red-500" : "text-[var(--color-block-purple)]"
        }
      />

      {unreadCount > 0 && (
        <span className="absolute -top-2 -right-2 bg-red-500 text-white text-[10px] px-1.5 py-0.5 rounded-full">
          {unreadCount}
        </span>
      )}
    </Link>
  );
}
