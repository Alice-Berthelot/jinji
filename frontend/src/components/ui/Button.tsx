interface ButtonProps {
  title: string;
  type?: "button" | "submit";
  isLoading?: boolean;
  disabled?: boolean;
  onClick?: () => void;
  marginTop?: string;
  marginRight?: string;
  paddingY?: string;
  className?: string;
}

export default function Button({
  title,
  type = "button",
  isLoading = false,
  disabled = false,
  onClick,
  marginTop,
  marginRight,
  paddingY,
  className = "",
}: ButtonProps) {
  return (
    <button
      type={type}
      disabled={disabled || isLoading}
      onClick={onClick}
      aria-busy={isLoading}
      className={`w-48 ${paddingY ? paddingY : "py-3"} ${marginTop ? marginTop : "mt-4"} ${marginRight ? marginRight : "mr-4"} bg-[var(--color-block-purple)] hover:bg-[var(--color-block-purple-hover)] hover:tracking-wide transition rounded-full text-center font-medium ${disabled || isLoading ? "opacity-50 cursor-not-allowed" : "cursor-pointer"} ${className}`}
    >
      {isLoading ? "Chargement..." : title}
    </button>
  );
}
