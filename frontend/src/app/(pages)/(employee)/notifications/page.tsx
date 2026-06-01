import BackArrow from "@/components/ui/BackArrow";
import MainTitle from "@/components/ui/MainTitle";
import NotificationsList from "@/components/NotificationsList";
import { getNotifications } from "@/services/notification.service";

export default async function NotificationsPage({
  searchParams,
}: {
  searchParams: Promise<{ page?: string }>;
}) {
  const params = await searchParams;

  const page = Number(params.page ?? 0);

  const notifications = await getNotifications(page, 10);

  return (
    <>
      <BackArrow />

      <MainTitle title="Mes notifications" />

      <section className="m-auto lg:my-0 lg:mx-8 bg-[var(--color-block-white)] px-6 py-4 shadow-sm rounded-sm w-[95%] min-h-screen">
        <div className="flex flex-col justify-center items-center gap-4 lg:gap-8 mb-6">
          <NotificationsList
            notifications={notifications.content}
            page={notifications.number}
            totalPages={notifications.totalPages}
            hasNext={!notifications.last}
            hasPrevious={!notifications.first}
          />
        </div>
      </section>
    </>
  );
}
