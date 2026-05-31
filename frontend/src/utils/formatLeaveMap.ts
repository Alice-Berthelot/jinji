import { LeaveCalendar } from "@/types/leave/leave";
import { eachDayOfInterval, format, parseISO } from "date-fns";

export function buildLeaveMap(leaves: LeaveCalendar[]) {
  const map: Record<string, LeaveCalendar[]> = {};

  for (const leave of leaves) {
    const days = eachDayOfInterval({
      start: parseISO(leave.startDate),
      end: parseISO(leave.endDate),
    });

    for (const d of days) {
      const key = format(d, "yyyy-MM-dd");

      if (!map[key]) map[key] = [];
      map[key].push(leave);
    }
  }

  return map;
}