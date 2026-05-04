export function formatDate(dateString: string) {
  if (!dateString) return "";

  const date = new Date(dateString);

  return new Intl.DateTimeFormat("fr-FR", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
  }).format(date);
}
