import { deleteTokens } from "@/lib/auth"
import { redirect } from "next/navigation"

 export async function logout() {
  await deleteTokens()
  redirect("/login");
}