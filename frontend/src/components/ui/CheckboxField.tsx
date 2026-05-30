type CheckboxFieldProps = {
  label: string;
  name: string;
  value: string | number;
  checked?: boolean;
  onCheckedChange?: (checked: boolean, value: string | number) => void;
};

export function CheckboxField({
  label,
  value,
  checked,
  onCheckedChange,
}: CheckboxFieldProps) {
  return (
    <label className="flex items-center gap-2 cursor-pointer">
      <input
        type="checkbox"
        className="sr-only"
        checked={checked}
        onChange={(e) => onCheckedChange?.(e.target.checked, value)}
      />

      <div
        className={`
            w-4 h-4 rounded border transition
            flex items-center justify-center
            ${
              checked
                ? "bg-[var(--color-block-purple)] border-[var(--color-block-purple)]"
                : "bg-white border-gray-400"
            }
          `}
      >
        {checked && <span className="text-xs">✓</span>}
      </div>

      <span>{label}</span>
    </label>
  );
}
