type CreateEmployeePayload = {
    employeeNumber: string;
    surname: string;
    firstName: string;
    email: string;
    phoneNumber?: string;
    seniorityDate?: string;
    departmentCode: string;
    createUser: boolean;
    password?: string;
    roles?: string[];
  };