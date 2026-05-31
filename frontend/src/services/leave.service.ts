import apiFetch from "@/lib/apiFetch";
import { LeaveCalendar } from "@/types/leave/leave";
import { PageView } from "@/types/pageView";

export async function getMyLeaves(): Promise<LeaveCalendar[]> {
  return apiFetch<LeaveCalendar[]>("/api/leaves/me");
}

export async function getLeaves(pageType: PageView): Promise<LeaveCalendar[]> {
  return apiFetch<LeaveCalendar[]>(`/api/leaves/?pageType=${pageType}`);
}
