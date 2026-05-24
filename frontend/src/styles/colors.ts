export const bgColors: Record<Color, string> = {
  purple:
    "bg-[var(--color-block-purple)] hover:bg-[var(--color-block-purple-hover)]",
  red:
    "bg-[var(--color-block-red)] hover:bg-[var(--color-block-red-hover)]",
  lightPurple: "bg-[var(--color-block-purple-hover)] hover:bg-[var(--color-dark-purple)]"
};

export const bgStatusColors: Record<string, string> = {
  APPROVED: "bg-[var(--color-light-green)]",
  REJECTED: "bg-[var(--color-block-red)]",
  PENDING: "bg-[var(--color-orange)]",
  CANCELLED: "bg-[var(--color-bg)]",
};