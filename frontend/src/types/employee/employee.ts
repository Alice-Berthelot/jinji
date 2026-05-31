export interface EmployeeProfile {
    employeeNumber: string;
    surname: string;
    firstName: string;
    email: string;
    phoneNumber?: string;
    seniorityDate: string;
    departmentCode?: string;
    departmentName?: string;
    teams?: string[];
  };

  export type EmployeeDetails = {
    id: number;
    employeeNumber?: string;
    surname: string;
    firstName: string;
    email: string;
    phoneNumber?: string;
    seniorityDate: string;
    departmentName: string;
    teams?: string[];
  };

export type EmployeeFullName = {
  surname: string;
  firstName: string;
}

export type EmployeePageView = "HR" | "MANAGER";

export interface EmployeeTable {
  id: number;
  employeeNumber: string;
  surname: string;
  firstName: string;
  email: string;
  phoneNumber: string;
  seniorityDate: string;
  departmentName: string;
  teams: string[];
}
