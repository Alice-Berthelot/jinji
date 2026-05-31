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
import { SelectField } from "../ui/SelectField";
import { toast } from "react-toastify";
import { useRouter } from "next/navigation";
import Subtitle from "../ui/Subtitle";
import { Team } from "@/types/employee/team";
import { CheckboxField } from "../ui/CheckboxField";

type NewEmployeeFormProps = {
  departments: Department[];
  teams: Team[];
};

export default function NewEmployeeForm({
  departments,
  teams,
}: NewEmployeeFormProps) {
  const [state, formAction] = useActionState<EmployeeState, FormData>(
    createEmployeeAction,
    { error: null }
  );

  const [createUser, setCreateUser] = useState(true);
  const [selectedTeams, setSelectedTeams] = useState<number[]>([]);
  const [managerOfTeams, setManagerOfTeams] = useState<number[]>([]);

  const router = useRouter();

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
      router.push("/hr/employees");
    }

    if (state.error) {
      toast.error(state.error);
    }
  }, [state]);

  const toggleTeam = (checked: boolean, value: number | string) => {
    const id = Number(value);

    setSelectedTeams((prev) =>
      checked ? [...prev, id] : prev.filter((t) => t !== id)
    );
  };

  const toggleManagerTeam = (checked: boolean, value: number | string) => {
    const id = Number(value);

    setManagerOfTeams((prev) => {
      if (checked) {
        if (!selectedTeams.includes(id)) {
          setSelectedTeams((prevTeams) => [...prevTeams, id]);
        }

        return [...prev, id];
      }

      return prev.filter((t) => t !== id);
    });
  };

  const selectedTeamObjects = teams.filter((team) =>
    selectedTeams.includes(team.id)
  );

  const toggleCreateUser = (checked: boolean) => {
    setCreateUser(checked);
  };

  return (
    <form
      ref={formRef}
      onChange={handleChange}
      action={formAction}
      className="m-auto pl-2 lg:px-6 py-8 flex flex-col gap-6"
    >
      <Subtitle
        subtitle="Formulaire d'ajout de collaborateur"
        paddingLeft="pl-0 lg:pl-2"
        className="self-start"
      />
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

      <div className="flex flex-col gap-2 group">
        <label className="group-focus-within:font-bold">Equipe(s)</label>

        <article className="flex flex-row gap-6">
          {teams.map((team) => (
            <CheckboxField
              key={team.id}
              name="memberTeamIds"
              value={team.id}
              label={team.label}
              checked={selectedTeams.includes(team.id)}
              onCheckedChange={toggleTeam}
            />
          ))}
        </article>
      </div>

      {selectedTeamObjects.map((team) => (
        <div key={team.id} className="flex flex-col gap-2 group">
          <label className="group-focus-within:font-bold">
            Equipe(s) managée(s) par le collaborateur
          </label>

          <CheckboxField
            name="managerTeamIds"
            value={team.id}
            label={team.label}
            checked={managerOfTeams.includes(team.id)}
            onCheckedChange={toggleManagerTeam}
          />
        </div>
      ))}

      <input
        type="hidden"
        name="createUser"
        value={createUser ? "true" : "false"}
      />

      <CheckboxField
        name="createUser"
        value="create-user"
        label="Créer un compte utilisateur ?"
        checked={createUser}
        onCheckedChange={(checked) => toggleCreateUser(checked)}
      />

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
