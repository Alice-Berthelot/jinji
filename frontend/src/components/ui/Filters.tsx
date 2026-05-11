type FilterOption<T extends string> = {
  label: string;
  value: T;
};

interface FiltersProps<T extends string> {
  options: readonly FilterOption<T>[];
  value: T;
  onChange: (value: T) => void;
}

export default function Filters<T extends string>({
  options,
  value,
  onChange,
}: FiltersProps<T>) {
  return (
    <section className="flex gap-2 justify-start">
      {options.map((option) => (
        <button
          key={option.value}
          onClick={() => onChange(option.value)}
          className={`${value === option.value ? "font-bold bg-[var(--color-dark-purple)] text-[var(--color-block-white)] px-4 rounded-full" : "font-normal text-[var(--color-dark-purple)] border border-solid border-[var(--color-dark-purple)] px-4 rounded-full"}`} 
        >
          {option.label}
        </button>
      ))}
    </section>
  );
}
