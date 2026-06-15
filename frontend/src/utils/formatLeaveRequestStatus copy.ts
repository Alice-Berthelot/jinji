import { EmployeeStatus } from "@/types/employee/employee";

const employeeStatusFrenchLabels: Record<EmployeeStatus, string> = {
  INTERNAL: "Interne",
  EXTERNAL: "Externe",
};

export function formatEmployeeStatus(status: EmployeeStatus) {
  return employeeStatusFrenchLabels[status] ?? status;
}