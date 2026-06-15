"use client";

import { useState, useTransition } from "react";
import { toast } from "react-toastify";

import { updateLeaveValidation } from "@/services/hrPolicy.service";
import Button from "./ui/Button";
import { LeaveValidation } from "@/types/leave/hrPolicy";
import { SelectField } from "./ui/SelectField";

type Props = {
  initialValue: LeaveValidation;
};

export default function LeaveValidationUpdate({ initialValue }: Props) {
  const [value, setValue] = useState<LeaveValidation>(initialValue);
  const [isPending, startTransition] = useTransition();

  const hasChanged = value !== initialValue;

  const handleSave = () => {
    startTransition(async () => {
      try {
        await updateLeaveValidation(value);
        toast.success("Règle mise à jour");
      } catch (err) {
        toast.error("Erreur lors de la mise à jour");
      }
    });
  };

  return (
    <div className="w-full max-w-xl space-y-6">

      <SelectField
        label="Validation des congés payés"
        name="leaveValidation"
        value={value}
        onChange={(e) =>
          setValue(e.target.value as LeaveValidation)
        }
        options={[
          { label: "Manager uniquement", value: "MANAGER_ONLY" },
          { label: "Manager puis RH", value: "MANAGER_THEN_HR" },
        ]}
      />

      <div className="flex justify-end">
        <Button
          title="Enregistrer"
          onClick={handleSave}
          isLoading={isPending}
          disabled={!hasChanged || isPending}
          className="w-auto px-6"
        />
      </div>

    </div>
  );
}