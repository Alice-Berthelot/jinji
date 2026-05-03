type Option = {
  label: string;
  value: string | number;
};

type RadioFieldProps = {
  label?: string;
  name: string;
  options: Option[];
  error?: string | null;
  errorText?: string;
  defaultValue?: string | number;
};

export function RadioField({
  label,
  name,
  options,
  error,
  errorText,
  defaultValue,
}: RadioFieldProps) {
  return (
    <div className="flex flex-col gap-2">
      <div className="flex">
        {label && <label className="font-medium">{label}</label>}

        {error && (
          <span className="text-red-600 ml-1" aria-hidden="true">
            *
          </span>
        )}
      </div>

      <div className="flex gap-6">
        {options.map((opt) => (
          <label key={opt.value} className="flex items-center gap-2">
            <input
              type="radio"
              name={name}
              value={opt.value}
              defaultChecked={defaultValue === opt.value}
            />
            {opt.label}
          </label>
        ))}
      </div>

      {error && errorText && (
        <p className="text-red-600 text-sm">{errorText}</p>
      )}
    </div>
  );
}
