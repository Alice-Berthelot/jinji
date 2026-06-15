"use client";

import { useEffect, useRef, useState } from "react";
import dynamic from "next/dynamic";
import "react-calendar/dist/Calendar.css";
import { format } from "date-fns";
import { fr } from "date-fns/locale";
import { LeaveCalendar } from "@/types/leave/leave";
import { cancelLeave } from "@/services/leave.service";
import { useRouter } from "next/navigation";
import { toast } from "react-toastify";
import Button from "../ui/Button";
import { formatDate } from "@/utils/formatDate";

const Calendar = dynamic(() => import("react-calendar"), { ssr: false });

type Props = {
  leaveMap: Record<string, LeaveCalendar[]>;
  manager?: boolean;
};

export default function Planning({ leaveMap, manager = false }: Props) {
  const [date, setDate] = useState<Date>(new Date());
  const router = useRouter();

  const [selectedDate, setSelectedDate] = useState<Date | null>(null);
  const [selectedLeaves, setSelectedLeaves] = useState<LeaveCalendar[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const dialogRef = useRef<HTMLDialogElement>(null);

  useEffect(() => {
    if (isModalOpen) {
      dialogRef.current?.showModal();
    } else {
      dialogRef.current?.close();
    }
  }, [isModalOpen]);

  function handleDayClick(date: Date) {
    const key = format(date, "yyyy-MM-dd");
    const leaves = leaveMap[key] ?? [];

    setSelectedDate(date);
    setSelectedLeaves(leaves);
    setIsModalOpen(true);
  }

  async function handleCancel(leaveId: number) {
    try {
      setIsSubmitting(true);

      await cancelLeave(leaveId);

      toast.success("Absence annulée avec succès");

      setSelectedLeaves((prev) => prev.filter((l) => l.leaveId !== leaveId));

      router.refresh();
    } catch {
      toast.error("Erreur lors de l'annulation de la demande");
    } finally {
      setIsSubmitting(false);
    }
  }

  const tileContent = ({ date, view }: { date: Date; view: string }) => {
    if (view !== "month") return null;

    const key = format(date, "yyyy-MM-dd");
    const leaves = (leaveMap[key] ?? []).filter(
      (leave) => leave.status !== "CANCELLED"
    );

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
        onClickDay={handleDayClick}
      />

      <dialog
        ref={dialogRef}
        onClose={() => setIsModalOpen(false)}
        className="rounded-xl p-0 shadow-lg backdrop:bg-black/40"
      >
        <div className="w-full max-w-md p-4 max-h-[80vh] overflow-auto">
          <div className="flex justify-between items-center mb-3">
            <h2 className="font-semibold">
              {selectedDate
                ? format(selectedDate, "dd MMMM yyyy", { locale: fr })
                : "Détails"}
            </h2>

            <button
              type="button"
              aria-label="Fermer la fenêtre"
              onClick={() => setIsModalOpen(false)}
              className="cursor-pointer"
            >
              ✕
            </button>
          </div>

          {selectedLeaves.length === 0 ? (
            <p className="text-sm text-gray-500">Aucune absence</p>
          ) : (
            <div className="flex flex-col gap-3">
              {selectedLeaves.map((leave, idx) => (
                <div
                  key={idx}
                  className="p-3 border rounded-lg flex justify-between items-center"
                >
                  <div>
                    <p className="font-medium">{leave.leaveType.label}</p>

                    <p className="text-xs text-gray-500">
                      {leave.firstName} {leave.surname}
                    </p>

                    <p className="text-xs text-gray-500">
                      {formatDate(leave.startDate)} →{" "}
                      {formatDate(leave.endDate)}
                    </p>
                  </div>
                  {!manager && (
                    <Button
                      title="Annuler"
                      isLoading={isSubmitting}
                      disabled={isSubmitting}
                      marginTop="mt-0"
                      paddingY="py-1"
                      width="w-32"
                      className="text-sm ml-4 bg-[var(--color-block-red)] hover:bg-[var(--color-block-red-hover)]"
                      onClick={() => handleCancel(leave.leaveId)}
                    />
                  )}
                </div>
              ))}
            </div>
          )}
        </div>
      </dialog>
    </div>
  );
}
