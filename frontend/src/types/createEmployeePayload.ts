export type CreateEmployeePayload = {
    employeeNumber: string;
    surname: string;
    firstName: string;
    email: string;
    phoneNumber?: string;
    seniorityDate?: string;
    departmentCode: string;
    memberTeamIds?: number[];
    managerTeamIds?: number[];
    createUser: boolean;
    password?: string;
    roles?: string[];
  };