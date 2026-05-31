"use client";

import { Column, Table } from "@/components/tables/Table";
import { EmployeeTable as EmployeeTableType } from "@/types/employee/employee";
import Link from "next/link";
import { useRouter } from "next/navigation";
import Subtitle from "../ui/Subtitle";

interface Props {
  employees: EmployeeTableType[];
  page: number;
  totalPages: number;
  hasNext: boolean;
  hasPrevious: boolean;
  search: string;
}

export default function EmployeeTable({
  employees,
  page,
  totalPages,
  hasNext,
  hasPrevious,
  search,
}: Props) {
  const router = useRouter();

  const columns: Column<EmployeeTableType>[] = [
    { header: "Matricule", accessor: "employeeNumber" },
    { header: "Nom", accessor: "surname" },
    { header: "Prénom", accessor: "firstName" },
    { header: "Adresse e-mail", accessor: "email" },
    { header: "Téléphone", accessor: "phoneNumber" },
    {
      header: "Ancienneté",
      accessor: (row) =>
        new Date(row.seniorityDate).toLocaleDateString("fr-FR"),
    },
    { header: "Département", accessor: "departmentName" },
    {
      header: "Equipe",
      accessor: (row) => row.teams.join(", "),
    },
  ];

  return (
    <section className="mt-8">
      <Subtitle subtitle="Liste des collaborateurs" />

      <div className="space-y-4">
        <Table
          columns={columns}
          data={employees}
          onRowClick={(employee) => router.push(`/hr/employees/${employee.id}`)}
        />
        <div className="flex justify-between items-center">
          {hasPrevious ? (
            <Link
              href={`?page=${page - 1}&search=${search}`}
              className="px-3 py-1 bg-gray-200 rounded"
            >
              Précédent
            </Link>
          ) : (
            <span className="text-gray-400">Précédent</span>
          )}

          <span>
            Page {page + 1} / {totalPages}
          </span>

          {hasNext ? (
            <Link
              href={`?page=${page + 1}&search=${search}`}
              className="px-3 py-1 bg-gray-200 rounded"
            >
              Suivant
            </Link>
          ) : (
            <span className="text-gray-400">Suivant</span>
          )}
        </div>
      </div>
    </section>
  );
}
