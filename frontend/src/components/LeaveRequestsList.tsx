"use client";

import { useMemo, useState } from "react";
import Link from "next/link";
import { LeaveRequestsSummary } from "@/types/leave/leaveRequest";
import Filters from "./ui/Filters";
import { formatDate } from "@/utils/formatDate";
import LinkCustom from "./ui/LinkCustom";

type Props = {
  leaveRequests: LeaveRequestsSummary[];
  role: Role;
  detailBasePath: string;
};

export default function LeaveRequestsList({
  leaveRequests,
  role,
  detailBasePath,
}: Props) {
  const [filter, setFilter] = useState<"all" | "processed" | "unprocessed">(
    "all"
  );

  const filterOptions = [
    { label: "Toutes", value: "all" },
    { label: "Non traitées", value: "unprocessed" },
    { label: "Traitées", value: "processed" },
  ] as const;

  const visibleLeaveRequests = useMemo(() => {
    return leaveRequests
      .map((request) => {
        const isManagerReviewed = request.hasManagerReview;
        const isHrReviewed = request.hasHrReview;

        const isCancelled = request.status === "CANCELLED";

        const isProcessed =
          role === "HR"
            ? isHrReviewed || isCancelled
            : isManagerReviewed || isCancelled;

        return {
          ...request,
          isProcessed,
        };
      })
      .filter((request) => {
        if (filter === "all") return true;
        if (filter === "processed") return request.isProcessed;
        if (filter === "unprocessed") return !request.isProcessed;

        return true;
      })
      .sort((a, b) => {
        if (a.isProcessed && !b.isProcessed) return 1;
        if (!a.isProcessed && b.isProcessed) return -1;

        return (
          new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
        );
      });
  }, [leaveRequests, filter, role]);

  return (
    <>
      <Filters options={filterOptions} value={filter} onChange={setFilter} />

      {visibleLeaveRequests.map((leaveRequest) => {
  const waitingForHr =
    leaveRequest.hasManagerReview &&
    !leaveRequest.hasHrReview &&
    leaveRequest.status !== "CANCELLED";

  const waitingForManager =
    !leaveRequest.hasManagerReview &&
    leaveRequest.status !== "CANCELLED";

  const highlightForCurrentRole =
    (role === "MANAGER" && waitingForHr) ||
    (role === "HR" && waitingForManager);

  return (
    <article
      key={leaveRequest.id}
      className={`${
        highlightForCurrentRole
          ? "bg-[var(--color-block-purple)]"
          : leaveRequest.isProcessed
          ? "bg-[var(--color-light-purple)]"
          : "bg-[var(--color-block-white)]"
      } p-4 shadow-sm rounded-sm w-[95%] lg:w-[45%] flex flex-col gap-2`}
    >
      <h3 className="font-semibold">
        Demande de {leaveRequest.employeeFirstName}{" "}
        {leaveRequest.employeeSurname} ({leaveRequest.leaveTypeLabel})
      </h3>

      <p className="text-sm">
        Du {formatDate(leaveRequest.startDate)} au{" "}
        {formatDate(leaveRequest.endDate)}
      </p>

      <p className="text-sm italic text-[var(--color-dark-purple)]">
        {leaveRequest.statusLabel}
      </p>

      <LinkCustom
        title="Voir"
        href={`${detailBasePath}/${leaveRequest.id}`}
        className="self-end"
      />
    </article>
  );
})}
    </>
  );
}
