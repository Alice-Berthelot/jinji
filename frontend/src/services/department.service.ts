import apiFetch from "@/lib/apiFetch";
import { Department } from "@/types/departments";

export async function getDepartments(): Promise<Department[]> {
  return apiFetch<Department[]>("/api/departments");
}