"use client";

import { useRouter, useSearchParams } from "next/navigation";

type PaginationProps = {
  page: number;
  totalPages: number;
  hasNext: boolean;
  hasPrevious: boolean;
};

export default function Pagination({
  page,
  totalPages,
  hasNext,
  hasPrevious,
}: PaginationProps) {
  const router = useRouter();
  const searchParams = useSearchParams();

  const changePage = (newPage: number) => {
    const params = new URLSearchParams(searchParams.toString());

    params.set("page", newPage.toString());

    router.push(`?${params.toString()}`);
  };

  return (
    <div className="flex items-center justify-center gap-4 mt-8">
      <button
        onClick={() => changePage(page - 1)}
        disabled={!hasPrevious}
        className="px-4 py-2 border rounded disabled:opacity-50"
      >
        Précédent
      </button>

      <span>
        Page {page + 1} / {totalPages}
      </span>

      <button
        onClick={() => changePage(page + 1)}
        disabled={!hasNext}
        className="px-4 py-2 border rounded disabled:opacity-50"
      >
        Suivant
      </button>
    </div>
  );
}