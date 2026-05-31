"use client";

import { useState } from "react";
import dynamic from "next/dynamic";
import "react-calendar/dist/Calendar.css";
import { format } from "date-fns";
import { LeaveCalendar } from "@/types/leave/leave";

const Calendar = dynamic(() => import("react-calendar"), { ssr: false });

type Props = {
  leaveMap: Record<string, LeaveCalendar[]>;
};

export default function Planning({ leaveMap }: Props) {
  const [date, setDate] = useState<Date>(new Date());

  const tileContent = ({ date, view }: { date: Date; view: string }) => {
    if (view !== "month") return null;

    const key = format(date, "yyyy-MM-dd");
    const leaves = leaveMap[key];

    if (!leaves?.length) return null;

    return (
      <div className="flex flex-col gap-1 mt-1 w-full">
        {leaves.slice(0, 3).map((leave, i) => (
          <div
            key={i}
            className={`h-2 w-full rounded ${
              leave.leaveType.code === "CP"
                ? "bg-[var(--color-dark-green)]"
                : leave.leaveType.code === "AM"
                ? "bg-[var(--color-dark-purple)]"
                : "bg-[var(--color-orange)]"
            }`}
            title={
              leave.firstName && leave.surname
                ? `${leave.firstName} ${leave.surname} - ${leave.leaveType.label}`
                : `${leave.leaveType.label}`
            }
          />
        ))}

        {leaves.length > 3 && (
          <span className="text-[10px] text-gray-500 leading-none">
            +{leaves.length - 3}
          </span>
        )}
      </div>
    );
  };

  const calendarStyle = `
    .react-calendar {
      border: none;
      width: 100%;
      background: transparent;
      font-family: inherit;
    }

    .react-calendar__navigation {
      margin-bottom: 1rem;
    }

    .react-calendar__navigation button {
      background: none;
      font-weight: 600;
      text-transform: capitalize;
    }

    .react-calendar__month-view__weekdays {
      text-transform: uppercase;
      font-size: 10px;
      opacity: 0.6;
      margin-bottom: 6px;
    }

    .react-calendar__month-view__days {
      display: grid !important;
      grid-template-columns: repeat(7, 1fr);
      gap: 6px;
    }

    .react-calendar__tile {
      height: 80px;
      max-width: none !important;
      border-radius: 12px;
      padding: 6px;
      display: flex;
      flex-direction: column;
      justify-content: flex-start;
      align-items: flex-start;
      background: #f9fafb;
      transition: all 0.15s ease;
    }

    .react-calendar__tile:hover {
      background: #eef2ff;
      transform: translateY(-1px);
    }

    .react-calendar__tile--now {
      background: #d3caff !important;
      border-radius: 12px;
      font-weight: bold;
    }

    .react-calendar__tile--active {
      background: #e0e7ff !important;
      border-radius: 12px;
    }
  `;

  return (
    <div className="p-4">
      <style>{calendarStyle}</style>

      <section className="flex gap-6 mb-4 text-sm">
        <div className="flex gap-2 items-center">
          <div className="w-3 h-3 rounded bg-[var(--color-dark-green)]" />
          <p>Congés payés</p>
        </div>

        <div className="flex gap-2 items-center">
          <div className="w-3 h-3 rounded bg-[var(--color-dark-purple)]" />
          <p>Arrêt maladie</p>
        </div>

        <div className="flex gap-2 items-center">
          <div className="w-3 h-3 rounded bg-[var(--color-orange)]" />
          <p>Autres absences</p>
        </div>
      </section>

      <Calendar
        selectRange={false}
        onChange={(value) => {
          if (value instanceof Date) setDate(value);
        }}
        value={date}
        tileContent={tileContent}
      />
    </div>
  );
}
