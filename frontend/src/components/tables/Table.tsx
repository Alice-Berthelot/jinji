"use client"

import React, { useRef, useState } from "react"

export interface Column<T> {
  header: string
  accessor: keyof T | ((row: T) => React.ReactNode)
  className?: string
}

interface TableProps<T> {
  columns: Column<T>[]
  data: T[]
  loading?: boolean
  onNext?: () => void
  onPrevious?: () => void
  hasNext?: boolean
  hasPrevious?: boolean
  onRowClick?: (row: T) => void 
}

export function Table<T>({
  columns,
  data,
  loading = false,
  onNext,
  onPrevious,
  hasNext,
  hasPrevious,
  onRowClick
}: TableProps<T>) {
  const containerRef = useRef<HTMLDivElement>(null)
  const [canScrollRight, setCanScrollRight] = useState(true)
  const [canScrollLeft, setCanScrollLeft] = useState(false)

  const scroll = (direction: "left" | "right") => {
    if (!containerRef.current) return
    const scrollAmount = 200
    if (direction === "right") {
      containerRef.current.scrollBy({ left: scrollAmount, behavior: "smooth" })
    } else {
      containerRef.current.scrollBy({ left: -scrollAmount, behavior: "smooth" })
    }

    setTimeout(() => {
      if (!containerRef.current) return
      setCanScrollLeft(containerRef.current.scrollLeft > 0)
      setCanScrollRight(
        containerRef.current.scrollLeft + containerRef.current.clientWidth <
          containerRef.current.scrollWidth
      )
    }, 300)
  }

  return (
    <div className="relative">
      {canScrollLeft && (
        <button
          onClick={() => scroll("left")}
          className="absolute left-0 top-1/2 -translate-y-1/2 z-10 bg-gray-200 p-2 rounded-full shadow"
        >
          ◀
        </button>
      )}

      <div
        ref={containerRef}
        className="overflow-x-hidden scrollbar-hide relative"
      >
        <table className="w-full text-sm border border-[var(--color-block-purple)] border-separate border-spacing-0 rounded-xl overflow-hidden shadow-sm min-w-max">
          <thead className="bg-[var(--color-block-purple)]">
            <tr>
              {columns.map((col, index) => (
                <th
                  key={index}
                  className={`text-center px-4 py-3 font-semibold ${col.className ?? ""}`}
                >
                  {col.header}
                </th>
              ))}
            </tr>
          </thead>

          <tbody className="divide-y divide-[var(--color-block-purple)]">
            {loading ? (
              <tr>
                <td colSpan={columns.length} className="text-center py-6">
                  Chargement...
                </td>
              </tr>
            ) : data.length === 0 ? (
              <tr>
                <td colSpan={columns.length} className="text-center py-6">
                  Aucune donnée
                </td>
              </tr>
            ) : (
              data.map((row, rowIndex) => (
                <tr
                  key={rowIndex}
                  className="hover:bg-gray-50 transition even:bg-[var(--color-light-purple)] cursor-pointer"
                  onClick={() => onRowClick?.(row)}
                >
                  {columns.map((col, colIndex) => (
                    <td key={colIndex} className={`text-center py-3 ${col.className ?? ""}`}>
                      {typeof col.accessor === "function"
                        ? col.accessor(row)
                        : (row[col.accessor] as React.ReactNode)}
                    </td>
                  ))}
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {canScrollRight && (
        <button
          onClick={() => scroll("right")}
          className="absolute right-0 top-1/2 -translate-y-1/2 z-10 bg-gray-200 p-2 rounded-full shadow"
        >
          ▶
        </button>
      )}

      {(onNext || onPrevious) && (
        <div className="flex justify-between items-center px-4 py-3 bg-gray-50">
          <button
            onClick={onPrevious}
            disabled={!hasPrevious}
            className="px-3 py-1 bg-gray-200 rounded disabled:opacity-50"
          >
            Précédent
          </button>

          <button
            onClick={onNext}
            disabled={!hasNext}
            className="px-3 py-1 bg-gray-200 rounded disabled:opacity-50"
          >
            Suivant
          </button>
        </div>
      )}
    </div>
  )
}