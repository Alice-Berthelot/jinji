"use client";

import { useActionState, useEffect, useRef, useState } from "react";
import { useRouter } from "next/navigation";
import { useFormStatus } from "react-dom";

import { createLeaveAction, LeaveState } from "@/app/actions/createLeave";

import ButtonPurple from "../ui/Button";
import { InputField } from "../ui/InputField";
import { SelectField } from "../ui/SelectField";
import { RadioField } from "../ui/RadioField";
import Subtitle from "../ui/Subtitle";
import LinkCustom from "../ui/LinkCustom";

import { LeaveType } from "@/types/leave/leaveTypes";

import { toast } from "react-toastify";

type NewLeaveFormProps = {
  employeeId: number;
  subtitle: string;
  leaveTypes: LeaveType[];
};

export default function NewLeaveForm({
  employeeId,
  subtitle,
  leaveTypes,
}: NewLeaveFormProps) {
  const [state, formAction] = useActionState<LeaveState, FormData>(
    createLeaveAction,
    { error: null }
  );

  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");

  const formRef = useRef<HTMLFormElement>(null);

  const [isValid, setIsValid] = useState(false);

  const router = useRouter();

  const today = new Date().toISOString().split("T")[0];

  const isStartDateValid = !startDate || startDate >= today;

  const isEndDateValid = !startDate || !endDate ? true : endDate >= startDate;

  useEffect(() => {
    if (state.success) {
      toast.success("Absence créée avec succès");
      router.push(`/hr/employees/${employeeId}`);
    }

    if (state.error) {
      toast.error(state.error);
    }
  }, [state, router, employeeId]);

  const handleChange = () => {
    if (formRef.current) {
      setIsValid(formRef.current.checkValidity());
    }
  };

  return (
    <form
      ref={formRef}
      action={formAction}
      onChange={handleChange}
      className="m-auto px-6 py-8 flex flex-col gap-6"
    >
      <Subtitle
        subtitle={subtitle}
        paddingLeft="pl-0 lg:pl-2"
        className="self-start"
      />

      <input type="hidden" name="employeeId" value={employeeId} />

      <div className="flex flex-col lg:flex-row gap-4 lg:items-center">
        <InputField
          label="Date de début"
          type="date"
          name="startDate"
          className="w-80"
          required
          min={today}
          onChange={(e) => setStartDate(e.target.value)}
        />

        <RadioField
          name="startPeriod"
          defaultValue="AM"
          options={[
            { value: "AM", label: "Matin" },
            { value: "PM", label: "Après-midi" },
          ]}
        />
      </div>

      {!isStartDateValid && (
        <p className="text-red-600 text-sm">
          La date de début ne peut pas être antérieure à aujourd'hui.
        </p>
      )}

      <div className="flex flex-col lg:flex-row gap-4 lg:items-center">
        <InputField
          label="Date de fin"
          type="date"
          name="endDate"
          className="w-80"
          required
          min={startDate || today}
          onChange={(e) => setEndDate(e.target.value)}
        />

        <RadioField
          name="endPeriod"
          defaultValue="PM"
          options={[
            { value: "AM", label: "Matin" },
            { value: "PM", label: "Après-midi" },
          ]}
        />
      </div>

      {!isEndDateValid && (
        <p className="text-red-600 text-sm">
          La date de fin ne peut pas être avant la date de début.
        </p>
      )}

      <div className="lg:w-96">
        <SelectField
          label="Type d'absence"
          name="leaveTypeCode"
          required
          options={leaveTypes.map((type) => ({
            value: type.code,
            label: type.label,
          }))}
        />
      </div>

      {state.error && (
        <p role="alert" className="text-red-600">
          {state.error}
        </p>
      )}

      <p className="italic text-xs mt-2">
        <span className="text-red-600">*</span> Champs obligatoires
      </p>

      <div className="flex flex-col md:flex-row items-center md:self-end">
        <ButtonPurple
          title="Ajouter"
          type="submit"
          isLoading={false}
          disabled={!isValid || !isStartDateValid || !isEndDateValid}
        />

        <LinkCustom
          href={`/employees/${employeeId}`}
          title="Annuler"
          color="red"
        />
      </div>
    </form>
  );
}
