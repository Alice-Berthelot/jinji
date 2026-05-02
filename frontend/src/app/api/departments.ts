"use server"

import { Department } from "@/types/departments";
import { cookies } from "next/headers";

  
  export async function getDepartments(): Promise<Department[]> {
    const cookieStore = await cookies();
    const token = cookieStore.get("access_token")?.value;
  
    const res = await fetch(
      `${process.env.NEXT_PUBLIC_API_URL}/api/departments`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        cache: "no-store",
      }
    );
  
    if (!res.ok) {
      throw new Error("Erreur lors du chargement des départements");
    }
  
    return res.json();
  }