"use client";

import { useActionState, useRef, useState } from "react";
import { useFormStatus } from "react-dom";
import { loginAction, LoginState } from "@/app/actions/login";
import ButtonPurple from "../ui/Button";
import { InputField } from "../ui/InputField";

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

  return (
    <form
      ref={formRef}
      onChange={handleChange}
      action={formAction}
      aria-describedby={state.error ? "form-error" : undefined}
      className="m-auto lg:self-center px-6 mt-10 py-8 border border-solid border-current border-[0.25px] rounded-xl flex flex-col gap-8 bg-[var(--color-block-white)] focus:ring-2 focus:ring-purple-600"
    >
      <InputField
        label="Identifiant"
        type="email"
        name="username"
        placeholder="Entrez votre identifiant"
        required
        error={state.error}
      />
      <InputField
        label="Mot de passe"
        type="password"
        id="password"
        name="password"
        minLength={12}
        placeholder="Entrez votre mot de passe"
        required
        error={state.error}
      />
      {state.error && (
        <p id="form-error" role="alert" className="text-red-600">
          {state.error}
        </p>
      )}
      <ButtonPurple
        title="Se connecter"
        type="submit"
        isLoading={pending}
        disabled={!isValid}
        className="self-end"
      />
    </form>
  );
}
