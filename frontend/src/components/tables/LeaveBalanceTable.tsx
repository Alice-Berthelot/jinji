"use client";

import { LeaveBalance } from "@/types/leave/leaveBalance";
import { useEffect, useState } from "react";
import { Column, Table } from "./Table";

interface LeaveBalanceTableProps {
  leaveBalance: LeaveBalance[] | [];
  loading: boolean;
}

export default function LeaveBalanceTable({
  leaveBalance,
  loading
}: LeaveBalanceTableProps) {

  const columns: Column<LeaveBalance>[] = [
    {
      header: "Période",
      accessor: (row) => (
        <div className="flex flex-col">
          <span>{row.label}</span>
          {row.label === "Congés 2025-2026" && (
            <span className="text-xs text-gray-500">
              En cours d'acquisition
            </span>
          )}
        </div>
      ),
    },
    {
      header: "Acquisition",
      accessor: (row) => {
        const start = new Date(row.acquisitionStartDate).toLocaleDateString(
          "fr-FR"
        );
        const end = new Date(row.acquisitionEndDate).toLocaleDateString("fr-FR");
        return `Du ${start} au ${end}`;
      },
      className: "hidden sm:table-cell",
    },
    {
      header: "Acquis",
      accessor: "acquiredDays",
    },
    {
      header: "Utilisés",
      accessor: "takenDays",
    },
    {
      header: "Restant",
      accessor: (row) => {
        const days = Number(row.remainingDays);
        return <span className="font-semibold">{days}</span>;
      },
    },
  ];

  return <Table columns={columns} data={leaveBalance} loading={loading} />;
}
