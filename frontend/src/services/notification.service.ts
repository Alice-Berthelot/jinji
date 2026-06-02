import apiFetch from "@/lib/apiFetch";
import { NotificationDTO } from "@/types/notification/notifications";
import { PageResponse } from "@/types/pagination/page";

export async function getNotifications(
  page: number,
  size: number = 10
): Promise<PageResponse<NotificationDTO>> {
  return apiFetch<PageResponse<NotificationDTO>>(
    `/api/notifications?page=${page}&size=${size}`
  );
}

export async function getUnreadCount(): Promise<number> {
  return apiFetch<number>("/api/notifications/unread-count");
}

export async function markNotificationAsRead(id: number): Promise<void> {
  await apiFetch(`/api/notifications/${id}/read`, {
    method: "PATCH",
  });
}

export async function deleteNotification(id: number): Promise<void> {
  await apiFetch(`/api/notifications/${id}`, {
    method: "DELETE",
  });
}
