"use client";

import { useState, useTransition } from "react";
import { useRouter } from "next/navigation";
import { NotificationDTO } from "@/types/notification/notifications";
import {
  deleteNotification,
  markNotificationAsRead,
} from "@/services/notification.service";

interface NotificationCardProps {
  notification: NotificationDTO;
}

export default function NotificationCard({
  notification,
}: NotificationCardProps) {
  const router = useRouter();

  const [isRead, setIsRead] = useState(notification.read);
  const [isPending, startTransition] = useTransition();

  async function handleRead() {
    if (isRead) return;

    await markNotificationAsRead(notification.id);

    setIsRead(true);

    startTransition(() => {
      router.refresh();
    });
  }

  const [deleted, setDeleted] = useState(false);

  async function handleDelete() {
    await deleteNotification(notification.id);
  
    setDeleted(true);
  
    startTransition(() => {
      router.refresh();
    });
  }
  
  if (deleted) return null;

  return (
    <li
      className={`
        p-4 shadow-sm rounded-sm w-[95%]
        flex flex-col gap-2 transition-colors
        ${
          isRead
            ? "bg-[var(--color-light-purple)]"
            : "bg-[var(--color-block-white)]"
        }
      `}
    >
      <p>{notification.message}</p>

      <p className="text-xs text-[var(--color-dark-purple)]">
        {new Date(notification.createdAt).toLocaleString("fr-FR")}
      </p>

      <div className="flex justify-end mr-4 lg:mr-7 gap-4 mt-2">
        {!isRead && (
          <button
            disabled={isPending}
            onClick={handleRead}
            className="
              bg-[var(--color-block-purple)]
              rounded-full
              px-6 py-1
              text-xs
              cursor-pointer
              hover:opacity-90
              disabled:opacity-50
            "
          >
            Marquer comme lue
          </button>
        )}

        <button
          onClick={handleDelete}
          className="
            bg-[var(--color-block-red)]
            rounded-full
            px-6 py-1
            text-xs
            cursor-pointer
            hover:opacity-90
          "
        >
          Supprimer
        </button>
      </div>
    </li>
  );
}
