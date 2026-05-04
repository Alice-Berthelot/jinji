"use client";

import { useActionState, useEffect, useRef, useState } from "react";
import { useRouter } from "next/navigation";
import { useFormStatus } from "react-dom";
import {
  createLeaveAction,
  LeaveState,
} from "@/app/actions/createLeaveRequest";
import { getLeaveTypes } from "@/app/api/leaveTypes";
import ButtonPurple from "../ui/Button";
import { InputField } from "../ui/InputField";
import { SelectField } from "../ui/SelectField";
import Subtitle from "../ui/Subtitle";
import { toast } from "react-toastify";
import LinkCustom from "../ui/LinkCustom";
import { LeaveType } from "@/types/leave/leaveTypes";
import { RadioField } from "../ui/RadioField";

export default function LeaveRequestForm() {
  const [state, formAction] = useActionState<LeaveState, FormData>(
    createLeaveAction,
    { error: null }
  );

  const [startDate, setStartDate] = useState<string>("");
  const [endDate, setEndDate] = useState<string>("");
  const today = new Date().toISOString().split("T")[0];
  const isStartDateValid = startDate >= today;
  const isEndDateValid =
  !startDate || !endDate ? true : endDate >= startDate;

  const validateDates = (start: string, end: string) => {
    if (start && start < today) return "start_invalid";
    if (start && end && end < start) return "end_invalid";
    return null;
  };

  const error = validateDates(startDate, endDate);

  const { pending } = useFormStatus();
  const router = useRouter();
  const formRef = useRef<HTMLFormElement>(null);
  const [isValid, setIsValid] = useState(false);

  const [leaveTypes, setLeaveTypes] = useState<LeaveType[]>([]);

  useEffect(() => {
    async function load() {
      try {
        const data = await getLeaveTypes();
        setLeaveTypes(data);
      } catch (err) {
        console.error(err);
      }
    }
    load();
  }, []);

  const handleChange = () => {
    if (formRef.current) {
      setIsValid(formRef.current.checkValidity());
    }
  };

  useEffect(() => {
    if (state.success) {
      toast.success("Demande de congé créée avec succès");
      router.push("/");
    }

    if (state.error) {
      toast.error(state.error);
    }
  }, [state]);

  const handleStartDateBlur = (e: React.FocusEvent<HTMLInputElement>) => {
    setStartDate(e.target.value);
  };
  
  const handleEndDateBlur = (e: React.FocusEvent<HTMLInputElement>) => {
    setEndDate(e.target.value);
  };

  return (
    <form
      ref={formRef}
      onChange={handleChange}
      action={formAction}
      className="m-auto px-6 py-8 flex flex-col gap-6"
    >
      <Subtitle
        subtitle="Créer une nouvelle demande d'absence"
        paddingLeft="pl-0 lg:pl-2"
        className="self-start"
      />

      <div className="flex gap-4 items-center">
        <InputField
          label="Date de début"
          type="date"
          name="startDate"
          className="w-96"
          onBlur={handleStartDateBlur}
          min={today}
          required
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

      {error === "start_invalid" && (
        <p className="text-red-600">
          La date de début ne peut pas être antérieure à aujourd'hui.
        </p>
      )}

      <div className="flex gap-4 items-center">
        <InputField
          label="Date de fin"
          type="date"
          name="endDate"
          className="w-96"
          required
          onBlur={handleEndDateBlur}
          min={startDate || today}
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
      {!isEndDateValid && endDate && (
        <p className="text-red-600 text-sm">
          La date de fin ne peut pas être antérieure à la date de début.
        </p>
      )}

      <SelectField
        label="Type de congé"
        name="leaveTypeCode"
        required
        options={leaveTypes.map((t) => ({
          value: t.code,
          label: t.label,
        }))}
      />

      <InputField label="Commentaire" type="text" name="employeeComment" />

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
          title="Envoyer"
          type="submit"
          isLoading={pending}
          disabled={!isValid || !isStartDateValid || !isEndDateValid}
        />
        <LinkCustom href="/leaves" title="Annuler" color="red" />
      </div>
    </form>
  );
}
