import { useState } from "react";

type Option = {
  label: string;
  value: string | number;
};

type Props = {
  label: string;
  options: Option[];
  value?: string | number;
  onChange: (value: string | number) => void;
};

export function MobileSelect({ label, options, value, onChange }: Props) {
  const [open, setOpen] = useState(false);

  const selectedLabel =
    options.find((o) => o.value === value)?.label || "Sélectionner...";

  return (
    <>
      {/* Trigger */}
      <button
        type="button"
        onClick={() => setOpen(true)}
        className="w-full border rounded px-3 py-2 text-left"
      >
        <div className="text-sm text-gray-500">{label}</div>
        <div>{selectedLabel}</div>
      </button>

      {/* Overlay */}
      {open && (
        <div
          className="fixed inset-0 bg-black/40 flex items-end"
          onClick={() => setOpen(false)}
        >
          {/* Bottom sheet */}
          <div
            className="w-full bg-white rounded-t-xl p-4 max-h-[70vh] overflow-auto"
            onClick={(e) => e.stopPropagation()}
          >
            <div className="mb-3 font-semibold">{label}</div>

            {options.map((opt) => (
              <button
                key={opt.value}
                onClick={() => {
                  onChange(opt.value);
                  setOpen(false);
                }}
                className="w-full text-left px-3 py-2 rounded hover:bg-gray-100"
              >
                {opt.label}
              </button>
            ))}
          </div>
        </div>
      )}
    </>
  );
}