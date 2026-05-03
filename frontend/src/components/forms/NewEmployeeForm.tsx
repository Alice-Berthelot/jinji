"use client";

import { useActionState, useRef, useState, useEffect } from "react";
import { useFormStatus } from "react-dom";
import ButtonPurple from "../ui/Button";
import { InputField } from "../ui/InputField";
import {
  createEmployeeAction,
  EmployeeState,
} from "@/app/actions/createEmployee";
import { Department } from "@/types/departments";
import { getDepartments } from "@/app/api/departments";
import { SelectField } from "../ui/SelectField";
import { toast } from "react-toastify";
import { useRouter } from "next/navigation";
import Subtitle from "../ui/Subtitle";

export default function NewEmployeeForm() {
  const [state, formAction] = useActionState<EmployeeState, FormData>(
    createEmployeeAction,
    { error: null }
  );

  const [createUser, setCreateUser] = useState(true);
  
  const router = useRouter();

  const [departments, setDepartments] = useState<Department[]>([]);

  useEffect(() => {
    async function load() {
      try {
        const data = await getDepartments();
        setDepartments(data);
      } catch (err) {
        console.error(err);
      }
    }

    load();
  }, []);

  const { pending } = useFormStatus();
  const formRef = useRef<HTMLFormElement>(null);
  const [isValid, setIsValid] = useState(false);

  const handleChange = () => {
    if (formRef.current) {
      setIsValid(formRef.current.checkValidity());
    }
  };

  useEffect(() => {
    if (state.success) {
      toast.success("Collaborateur créé avec succès");
      router.push("/");
    }

    if (state.error) {
      toast.error(state.error);
    }
  }, [state]);

  return (
    <form
      ref={formRef}
      onChange={handleChange}
      action={formAction}
      className="m-auto px-6 py-8 flex flex-col gap-6"
    >
      <Subtitle subtitle="Formulaire d'ajout de collaborateur" paddingLeft="pl-0 lg:pl-2" className="self-start"/>
      <InputField
        label="Numéro de matricule"
        type="text"
        name="employeeNumber"
        required
      />

      <InputField label="Nom de famille" type="text" name="surname" required />

      <InputField label="Prénom(s)" type="text" name="firstName" required />

      <InputField label="Adresse e-mail" type="email" name="email" required />

      <InputField label="Numéro de téléphone" type="text" name="phoneNumber" />

      <InputField
        label="Date d'ancienneté"
        type="date"
        name="seniorityDate"
        required
      />

      <SelectField
        label="Département"
        name="departmentCode"
        required
        options={departments.map((dept) => ({
          value: dept.code,
          label: dept.name,
        }))}
      />

<label className="flex items-center gap-2">
  <input
    type="checkbox"
    name="createUser"
    checked={createUser}
    onChange={(e) => setCreateUser(e.target.checked)}
  />
  Créer un compte utilisateur ?
</label>

{createUser && (
  <>
    <InputField
      label="Mot de passe"
      type="password"
      name="password"
      minLength={12}
      required
      disabled={!createUser}
    />

    <SelectField
      label="Rôle"
      name="role"
      required
      options={[
        { value: "EMPLOYEE", label: "Employé" },
        { value: "HR", label: "Ressources Humaines" },
        { value: "MANAGER", label: "Manager" },
      ]}
    />
  </>
)}

      {state.error && (
        <p role="alert" className="text-red-600">
          {state.error}
        </p>
      )}

      <p className="italic text-xs mt-2">
        <span className="text-red-600">*</span> Champs obligatoires
      </p>

      <ButtonPurple
        title="Ajouter"
        type="submit"
        isLoading={pending}
        disabled={!isValid}
        className="self-end"
      />
    </form>
  );
}
