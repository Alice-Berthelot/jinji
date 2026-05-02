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
    ...props
  }: SelectFieldProps) {
    const selectId = id || props.name;
  
    return (
      <div className="flex flex-col gap-2 group">
        <div className="flex">
          <label htmlFor={selectId} className="group-focus-within:font-bold">
            {label}
          </label>
  
          {props.required && (
            <span className="text-red-600 ml-1" aria-hidden="true">
              *
            </span>
          )}
        </div>
  
        <select
          id={selectId}
          {...props}
          className="border rounded px-3 py-2"
        >
          <option value="">Sélectionner...</option>
  
          {options.map((opt) => (
            <option key={opt.value} value={opt.value}>
              {opt.label}
            </option>
          ))}
        </select>
  
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