"use client";

import { useActionState, useRef, useState } from "react";
import { useFormStatus } from "react-dom";
import { loginAction, LoginState } from "@/app/actions/login";
import Button from "../ui/Button";

export default function LoginForm() {
  const [state, formAction] = useActionState<LoginState, FormData>(
    loginAction,
    { error: null }
  );
  const { pending } = useFormStatus();
  const formRef = useRef<HTMLFormElement>(null);
  const [isValid, setIsValid] = useState(false);

  const handleChange = () => {
    if (formRef.current) {
      setIsValid(formRef.current.checkValidity());
    }
  };

  const styleInput =
    "border border-solid border-current border-[0.25px] rounded-md px-4 py-2 focus:outline-none focus:border-[var(--color-dark-purple)] hover:border-[var(--color-dark-purple)] focus:bg-[var(--color-bg)]";

  return (
    <form
      ref={formRef}
      onChange={handleChange}
      action={formAction}
      aria-describedby={state.error ? "form-error" : undefined}
      className="m-auto lg:self-center px-6 mt-10 py-8 border border-solid border-current border-[0.25px] rounded-xl flex flex-col gap-8 bg-[var(--color-block-white)] focus:ring-2 focus:ring-purple-600"
    >
      <div className="flex flex-col gap-2 group">
        <label htmlFor="username" className="group-focus-within:font-bold">
          Identifiant
        </label>
        <input
          type="text"
          id="username"
          name="username"
          minLength={3}
          pattern="[a-zA-Z0-9_]+"
          placeholder="Entrez votre identifiant"
          required
          className={styleInput}
        />
      </div>
      <div className="flex flex-col gap-2 group">
        <label htmlFor="password" className="group-focus-within:font-bold">
          Mot de passe
        </label>
        <input
          type="password"
          id="password"
          name="password"
          minLength={8}
          placeholder="Entrez votre mot de passe"
          required
          className={styleInput}
        />
      </div>

      {state.error && (
        <p id="form-error" role="alert" className="text-red-600">
          {state.error}
        </p>
      )}
      <Button
        title="Se connecter"
        type="submit"
        isLoading={pending}
        disabled={!isValid}
        className="self-end"
      />
    </form>
  );
}
