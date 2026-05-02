"use client";

import { useRouter } from "next/navigation";
import { BsArrowLeft } from "react-icons/bs";

export default function BackArrow() {
  const router = useRouter();

  return (
  <button
    onClick={() => router.back()}
    className="flex items-center gap-2 text-[var(--color-main-font)] pl-2 mt-20 lg:hidden text"
    aria-label="Retour à la page précédente"
  >
    <BsArrowLeft size={30} />
  </button>
  )
}
