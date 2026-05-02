"use client";

import { useSearchParams } from "next/navigation";
import { useEffect } from "react";
import { toast } from "react-toastify";

export default function AuthToastHandler() {
  const searchParams = useSearchParams();
  const error = searchParams.get("error");

  useEffect(() => {
    if (!error) return;

    switch (error) {
      case "HR_REQUIRED":
        toast.error(
          "Accès réservé au service des Ressources Humaines. Vous avez été redirigé sur la page d'accueil."
        );
        break;

      case "MANAGER_REQUIRED":
        toast.error(
          "Accès réservé aux Managers. Vous avez été redirigé sur la page d'accueil."
        );
        break;

      case "NO_TOKEN":
        toast.error("Vous devez vous connecter");
        break;

      case "INVALID_TOKEN":
        toast.error("Session expirée, merci de vous reconnecter");
        break;

      default:
        toast.error("Accès non autorisé");
        break;
    }
  }, [error]);

  return null;
}