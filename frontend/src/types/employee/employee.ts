export type EmployeeStatus = "INTERNAL" | "EXTERNAL";

export interface EmployeeProfile {
    employeeNumber: string;
    surname: string;
    firstName: string;
    email: string;
    phoneNumber?: string;
    seniorityDate: string;
    departmentCode?: string;
    status: EmployeeStatus;
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
    status: EmployeeStatus;
    departmentName: string;
    teams?: string[];
  };

export type EmployeeFullName = {
  surname: string;
  firstName: string;
}

export interface EmployeeTable {
  id: number;
  employeeNumber: string;
  surname: string;
  firstName: string;
  email: string;
  phoneNumber: string;
  seniorityDate: string;
  status: EmployeeStatus;
  departmentName: string;
  teams: string[];
}
