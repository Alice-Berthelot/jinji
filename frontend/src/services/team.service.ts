import apiFetch from "@/lib/apiFetch";
import { Team } from "@/types/employee/team";

export async function getTeams(): Promise<Team[]> {
  return apiFetch<Team[]>("/api/team/summary");
}