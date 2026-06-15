import { useState } from "react";

type Option = {
  label: string;
  value: string | number;
};

type SelectFieldProps = {
  label: string;
  options: Option[];
  error?: string | null;
  errorText?: string;
  id?: string;
} & React.SelectHTMLAttributes<HTMLSelectElement>;

export function SelectField({
  label,
  options,
  error,
  errorText,
  id,
  value,
  onChange,
  name,
  required,
  ...props
}: SelectFieldProps) {
  const selectId = id || name;
  console.log("value:", value);
  const [open, setOpen] = useState(false);

  const selectedLabel =
  options.find((o) => String(o.value) === String(value))?.label ||
  "Sélectionner...";



  return (
    <div className="flex flex-col gap-2 group">
      {/* LABEL */}
      <div className="flex">
        <label htmlFor={selectId} className="group-focus-within:font-bold">
          {label}
        </label>

        {required && (
          <span className="text-red-600 ml-1" aria-hidden="true">
            *
          </span>
        )}
      </div>

      {/* ======================= */}
      {/* DESKTOP ONLY (UNCHANGED) */}
      {/* ======================= */}
      <div className="hidden md:block">
        <select
          id={selectId}
          value={value}
          onChange={onChange}
          name={name}
          required={required}
          {...props}
          className="border rounded px-3 py-2 w-full text-base"
        >
          {!props.multiple && <option value="">Sélectionner...</option>}

          {options.map((opt) => (
            <option key={opt.value} value={opt.value}>
              {opt.label}
            </option>
          ))}
        </select>
      </div>


      {/* ERROR */}
      {error && errorText && (
        <p
          id={`${selectId}-error`}
          role="alert"
          className="text-red-600 text-sm"
        >
          {errorText}
        </p>
      )}
    </div>
  );
}