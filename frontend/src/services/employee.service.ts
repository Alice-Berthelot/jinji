import apiFetch from "@/lib/apiFetch";
import { EmployeeFullName, EmployeeProfile } from "@/types/employee/employee";

export async function getMe(): Promise<EmployeeProfile> {
  return apiFetch<EmployeeProfile>("/api/employees/me");
}

export async function getMyFullName(): Promise<EmployeeFullName> {
  return apiFetch<EmployeeFullName>("/api/employees/me/fullname");
}