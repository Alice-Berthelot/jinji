export const timeOfDayLabels: Record<string, string> = {
  am: "matin",
  pm: "après-midi",
};

export function formatPeriod(value: string) {
  const key = value.toLowerCase();

  if (key in timeOfDayLabels) {
    return timeOfDayLabels[key];
  }

  return value;
}
