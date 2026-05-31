export type EmployeeProfile = {
    employeeNumber: string;
    surname: string;
    firstName: string;
    email: string;
    phoneNumber?: string;
    seniorityDate: string;
    departmentCode: string;
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
  seniorityDate: string; // ISO string côté API
  departmentName: string;
  teams: string[];
}