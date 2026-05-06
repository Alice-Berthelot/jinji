"use client"

import { MyLeaveRequestsSummary } from "@/types/leave/leaveRequest";
import { Column, Table } from "./Table";
import Link from "next/link";
import { IoOpenOutline } from "react-icons/io5";
import { formatDate } from "@/utils/formatDate";
import { formatLeaveRequestStatus } from "@/utils/formatLeaveRequestStatus";
import { useState } from "react";

type LeaveRequestSmallTableProps = {
  leaveRequests: MyLeaveRequestsSummary[] | [];
  loading: boolean;
};

export default function LeaveRequestSmallTable({
  leaveRequests,
  loading,
}: LeaveRequestSmallTableProps) {
    const [offset, setOffset] = useState(0);
    const [count, setCount] = useState(0);
  
    const limit = 5;

    const columns: Column<MyLeaveRequestsSummary>[] = [
        {
          header: "N° de la demande",
          accessor: (row) => (
            <Link
              href={`/leaves/leave-requests/${row.id}`}
              className="text-[var(--color-dark-purple)] underline underline-offset-2"
            >
              <div className="flex gap-2 items-center">
                <span>{row.id}</span>
                <IoOpenOutline size={15} />
              </div>
            </Link>
          ),
        },
        {
            header: "Type",
            accessor: (row) =>
              `${row.leaveTypeLabel}`,
          },
        {
          header: "Période concernée",
          accessor: (row) =>
            `Du ${formatDate(row.startDate)} au ${formatDate(row.endDate)}`,
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
        <Table
          columns={columns}
          data={leaveRequests}
          loading={loading}
          onNext={() => setOffset((prev) => prev + limit)}
          onPrevious={() => setOffset((prev) => Math.max(prev - limit, 0))}
          hasNext={offset + limit < count}
          hasPrevious={offset > 0}
        />
      );

}
