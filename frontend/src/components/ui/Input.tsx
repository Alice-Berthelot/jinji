type InputProps = React.InputHTMLAttributes<HTMLInputElement>;

export default function Input({ className = "", ...props }: InputProps) {
  const baseStyle =
    "border border-solid border-current border-[0.25px] rounded-md px-4 py-2 focus:outline-none focus:border-[var(--color-dark-purple)] hover:border-[var(--color-dark-purple)] focus:bg-[var(--color-bg)]";

  return <input {...props} className={`${baseStyle} ${className}`} />;
}