"use server";

import { getOptionalString, getString } from "@/utils/formData";
import { cookies } from "next/headers";
import { redirect } from "next/navigation";

export type EmployeeState = {
  error: string | null;
  success?: boolean;
};

export async function createEmployeeAction(
  prevState: EmployeeState,
  formData: FormData
): Promise<EmployeeState> {
  const cookieStore = await cookies();
  const token = cookieStore.get("access_token")?.value;

  const createUser = formData.get("createUser") === "on";

  // const payload : CreateEmployeePayload = {
  //   employeeNumber: formData.get("employeeNumber"),
  //   surname: formData.get("surname"),
  //   firstName: formData.get("firstName"),
  //   email: formData.get("email"),
  //   phoneNumber: formData.get("phoneNumber"),
  //   seniorityDate: formData.get("seniorityDate"),
  //   departmentCode: formData.get("departmentCode"),
  //   createUser,
  // };

  const payload: CreateEmployeePayload = {
    employeeNumber: getString(formData, "employeeNumber"),
    surname: getString(formData, "surname"),
    firstName: getString(formData, "firstName"),
    email: getString(formData, "email"),
    phoneNumber: getOptionalString(formData, "phoneNumber"),
    departmentCode: getString(formData, "departmentCode"),
    seniorityDate: getString(formData, "seniorityDate"),
    createUser,
  };
  
  if (createUser) {
    payload.password = getString(formData, "password");
    payload.roles = [getString(formData, "role")];
  }

  const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/employees`, {
    method: "POST",
    headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    body: JSON.stringify(payload),
  });

  console.log(res.status);

  if (!res.ok) {
    return { error: "Erreur lors de la création du collaborateur" };
  }

  return { error: null, success: true };
}
