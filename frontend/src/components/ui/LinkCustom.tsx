import { bgColors } from "@/styles/colors";
import Link from "next/link";

interface LinkCustomProps {
  href: string;
  title: string;
  color?: Color;
  isLoading?: boolean;
  disabled?: boolean;
  className?: string;
}

export default function LinkCustom({
  href,
  title,
  color = "purple",
  isLoading = false,
  disabled = false,
  className = "",
}: LinkCustomProps) {
  const isDisabled = disabled || isLoading;

  const bgColor = bgColors[color];;

  return (
    <Link
      href={isDisabled ? "#" : href}
      className={`inline-block w-48 py-3 mt-4 mr-4 ${bgColor} hover:tracking-wide transition rounded-full text-center font-medium ${
        isDisabled
          ? "opacity-50 cursor-not-allowed pointer-events-none"
          : "cursor-pointer"
      } ${className}`}
    >
      {isLoading ? "Chargement..." : title}
    </Link>
  );
}