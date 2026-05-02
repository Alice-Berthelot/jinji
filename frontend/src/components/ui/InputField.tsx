import Input from "./Input";

type InputFieldProps = {
  label: string;
  error?: string | null;
  errorText?: string;
  id?: string;
} & React.InputHTMLAttributes<HTMLInputElement>;

export function InputField({
  label,
  error,
  errorText,
  id,
  ...props
}: InputFieldProps) {
  const inputId = id || props.name;

  return (
    <div className="flex flex-col gap-2 group">
      <div className="flex">

      <label htmlFor={inputId} className="group-focus-within:font-bold">
        {label}
      </label>
      {props.required && (
        <span className="text-red-600 ml-1" aria-hidden="true">
          *
        </span>
      )}
      </div>

      <Input id={inputId} {...props} />

      {error && errorText && (
        <p
          id={`${inputId}-error`}
          role="alert"
          className="text-red-600 text-sm"
        >
          {errorText}
        </p>
      )}
    </div>
  );
}
