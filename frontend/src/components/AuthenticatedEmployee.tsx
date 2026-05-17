"use client";

import { EmployeeFullName } from "@/types/employee/employee";
import { usePathname } from "next/navigation";
import { useState } from "react";


type AuthenticatedEmployeeProps = {
  employeeFullName: EmployeeFullName | null;
};

export default function AuthenticatedEmployee({ employeeFullName }: AuthenticatedEmployeeProps) {
  const pathname = usePathname();

  if (pathname === "/login") {
    return null;
  }

  return (
    <div className="flex flex-col gap-0 items-end">
      <p className="text-xs">Connecté(e) en tant que </p>
      <p className="font-semibold text-sm"> {employeeFullName?.firstName} {employeeFullName?.surname}</p>
    </div>
  );
}
