"use client";

import { useActionState } from "react";
import { useFormStatus } from "react-dom";
import { loginAction, LoginState } from "@/app/actions/login";

export default function LoginForm() {
  const [state, formAction] = useActionState<LoginState, FormData>(
    loginAction,
    { error: null }
  );
  const { pending } = useFormStatus();

  return (
    <form action={formAction}>
      <label htmlFor="username">Identifiant</label>
      <input
        type="text"
        id="username"
        name="username"
        minLength={3}
        pattern="[a-zA-Z0-9_]+"
        required
      />
      <label htmlFor="password">Mot de passe</label>
      <input
        type="password"
        id="password"
        name="password"
        minLength={8}
        required
      />

      {state.error && <p style={{ color: "red" }}>{state.error}</p>}
      <button type="submit" disabled={pending}>
        {pending ? "Connexion..." : "Se connecter"}
      </button>
    </form>
  );
}
