export function getString(formData: FormData, key: string): string {
  const value = formData.get(key);

  if (!value || typeof value !== "string") {
    throw new Error(`Invalid key ${key}`);
  }

  return value;
}

export function getOptionalString(
  formData: FormData,
  key: string
): string | undefined {
  const value = formData.get(key);

  if (!value || typeof value !== "string") return undefined;

  return value;
}
