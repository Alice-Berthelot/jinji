import apiFetch from "@/lib/apiFetch";
import {
  EmployeeDetails,
  EmployeeFullName,
  EmployeeProfile,
  EmployeeTable,
} from "@/types/employee/employee";
import { PageView } from "@/types/pageView";
import { PageResponse } from "@/types/pagination/page";

export async function getMe(): Promise<EmployeeProfile> {
  return apiFetch<EmployeeProfile>("/api/employees/me");
}

export async function getMyFullName(): Promise<EmployeeFullName> {
  return apiFetch<EmployeeFullName>("/api/employees/me/fullname");
}

export async function getEmployeeById(
  employeeId: number,
  pageType: PageView
): Promise<EmployeeDetails> {
  return apiFetch<EmployeeDetails>(
    `/api/employees/${employeeId}?pageType=${pageType}`
  );
}

export async function getEmployeeFullNameById(
  employeeId: number
): Promise<EmployeeFullName> {
  return apiFetch<EmployeeDetails>(
    `/api/employees/${employeeId}/fullname`
  );
}

export async function getEmployees(
  page: number,
  size: number = 10,
  search: string = ""
): Promise<PageResponse<EmployeeTable>> {
  return apiFetch<PageResponse<EmployeeTable>>(
    `/api/employees/all?page=${page}&size=${size}&search=${search}`
  );
}
