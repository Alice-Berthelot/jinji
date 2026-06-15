"use client";

import { MyLeaveRequestsSummary } from "@/types/leave/leaveRequest";
import { PageResponse } from "@/types/pagination/page";
import { formatDate } from "@/utils/formatDate";
import { formatLeaveRequestStatus } from "@/utils/formatLeaveRequestStatus";
import Link from "next/link";
import { IoOpenOutline } from "react-icons/io5";
import { Column, Table } from "./Table";

type Props = {
  leaveRequests: PageResponse<MyLeaveRequestsSummary>;
};

export default function LeaveRequestSmallTable({
  leaveRequests,
}: Props) {
  const columns: Column<MyLeaveRequestsSummary>[] = [
    {
      header: "N°",
      accessor: (row) => (
        <Link
          href={`/leaves/leave-requests/${row.id}`}
          className="text-[var(--color-dark-purple)] underline"
        >
          <div className="flex gap-2 items-center justify-center">
            <span>{row.id}</span>
            <IoOpenOutline size={15} />
          </div>
        </Link>
      ),
    },
    {
      header: "Type",
      accessor: (row) => row.leaveTypeLabel,
    },
    {
      header: "Période",
      accessor: (row) =>
        `${formatDate(row.startDate)} - ${formatDate(row.endDate)}`,
    },
    {
      header: "Statut",
      accessor: (row) => {
        const colors: Record<string, string> = {
          PENDING: "bg-yellow-100 text-yellow-700",
          APPROVED: "bg-green-100 text-green-700",
          REJECTED: "bg-red-100 text-red-700",
          CANCELLED: "bg-gray-200 text-gray-600",
        };

        return (
          <span
            className={`px-2 py-1 rounded-md text-xs font-medium ${
              colors[row.status] ?? "bg-gray-100 text-gray-700"
            }`}
          >
            {formatLeaveRequestStatus(row.status)}
          </span>
        );
      },
    },
  ];

  return (
    <section>
      <Table
        columns={columns}
        data={leaveRequests.content}
      />

      <div className="flex justify-between items-center mt-4">
        {leaveRequests.first ? (
          <span className="text-gray-400">Précédent</span>
        ) : (
          <Link
            href={`?page=${leaveRequests.number - 1}`}
            className="px-3 py-1 bg-gray-200 rounded"
          >
            Précédent
          </Link>
        )}

        <span>
          Page {leaveRequests.number + 1} / {leaveRequests.totalPages}
        </span>

        {leaveRequests.last ? (
          <span className="text-gray-400">Suivant</span>
        ) : (
          <Link
            href={`?page=${leaveRequests.number + 1}`}
            className="px-3 py-1 bg-gray-200 rounded"
          >
            Suivant
          </Link>
        )}
      </div>
    </section>
  );
}