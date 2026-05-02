import Link from "next/link";

interface LinkPurpleProps {
  href: string;
  title: string;
  isLoading?: boolean;
  disabled?: boolean;
  className?: string;
}

export default function LinkPurple({
  href,
  title,
  isLoading = false,
  disabled = false,
  className = "",
}: LinkPurpleProps) {
  const isDisabled = disabled || isLoading;

  return (
    <Link
      href={isDisabled ? "#" : href}
      className={`inline-block w-48 py-3 mt-4 mr-4 bg-[var(--color-block-purple)] hover:bg-[var(--color-block-purple-hover)] hover:tracking-wide transition rounded-full text-center font-medium ${
        isDisabled
          ? "opacity-50 cursor-not-allowed pointer-events-none"
          : "cursor-pointer"
      } ${className}`}
    >
      {isLoading ? "Chargement..." : title}
    </Link>
  );
}