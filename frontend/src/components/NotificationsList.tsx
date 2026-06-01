"use client";

import { useMemo, useState } from "react";
import NotificationCard from "./NotificationCard";
import Filters from "@/components/ui/Filters";
import { NotificationDTO } from "@/types/notification/notifications";

type Props = {
  notifications: NotificationDTO[];
  page: number;
  totalPages: number;
  hasNext: boolean;
  hasPrevious: boolean;
};

export default function NotificationsList({ notifications }: Props) {
  const [filter, setFilter] = useState<"all" | "read" | "unread">("all");

  const filterOptions = [
    { label: "Toutes", value: "all" },
    { label: "Lues", value: "read" },
    { label: "Non lues", value: "unread" },
  ] as const;

  const visibleNotifications = useMemo(() => {
    return notifications.filter((n) => {
      if (filter === "all") return true;
      if (filter === "read") return n.read;
      if (filter === "unread") return !n.read;
      return true;
    });
  }, [notifications, filter]);

  return (
    <>
      <Filters options={filterOptions} value={filter} onChange={setFilter} />

      {visibleNotifications.length === 0 ? (
        <p className="text-center mt-4">Aucune notification à afficher</p>
      ) : (
        <ul className="flex flex-col items-center gap-4 mt-4 lg:w-2/5">
          {visibleNotifications.map((notification) => (
            <NotificationCard
              key={notification.id}
              notification={notification}
            />
          ))}
        </ul>
      )}
    </>
  );
}
