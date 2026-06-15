"use server";

import apiFetch from "@/lib/apiFetch";
import { CreateEmployeePayload } from "@/types/createEmployeePayload";
import { getOptionalString, getString } from "@/utils/formData";

export type EmployeeState = {
  error: string | null;
  success?: boolean;
};

export async function createEmployeeAction(
  prevState: EmployeeState,
  formData: FormData
): Promise<EmployeeState> {
  const createUser = formData.get("createUser") === "true";

  const payload: CreateEmployeePayload = {
    employeeNumber: getString(formData, "employeeNumber"),
    surname: getString(formData, "surname"),
    firstName: getString(formData, "firstName"),
    email: getString(formData, "email"),
    phoneNumber: getOptionalString(formData, "phoneNumber"),
    departmentCode: getString(formData, "departmentCode"),
    memberTeamIds: formData.getAll("memberTeamIds").map(Number),
    managerTeamIds: formData.getAll("managerTeamIds").map(Number),
    seniorityDate: getString(formData, "seniorityDate"),
    status: getOptionalString(formData, "status"),
    createUser,
  };

  if (createUser) {
    payload.password = getString(formData, "password");
  }

  try {
    await apiFetch("/api/employees", {
      method: "POST",
      body: JSON.stringify(payload),
    });

    return { error: null, success: true };
  } catch (e: unknown) {
    let message = "Erreur lors de la création du collaborateur";
  
    if (e instanceof Error) {
      message = e.message;
    }
  
    return {
      error: message,
      success: false,
    };
  }
}
