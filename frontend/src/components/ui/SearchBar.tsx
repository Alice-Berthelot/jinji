"use client";

import { useRouter, useSearchParams } from "next/navigation";
import { useState } from "react";
import Button from "./Button";
import Subtitle from "./Subtitle";
import Input from "./Input";
import { AiOutlineCloseCircle } from "react-icons/ai";

export default function SearchBar({ search }: { search: string }) {
  const router = useRouter();
  const params = useSearchParams();

  const [value, setValue] = useState(search);

  const onSearch = () => {
    const query = new URLSearchParams(params.toString());

    query.set("search", value);
    query.set("page", "0");

    router.push(`?${query.toString()}`);
  };

  const onClear = () => {
    setValue("");
  
    const query = new URLSearchParams(params.toString());
  
    query.delete("search");
    query.set("page", "0");
  
    router.push(`?${query.toString()}`);
  };

  return (
    <section>
      <Subtitle subtitle="Rechercher un collaborateur" />
      <div className="flex flex-col lg:flex-row gap-4 lg:items-center w-full lg:w-3/5">
        <div className="flex lg:w-full gap-2">
          <label htmlFor="search" className="sr-only">Entrer un nom, prénom ou e-mail de collaborateur</label>
        <Input
        id="search"
          value={value}
          onChange={(e) => setValue(e.target.value)}
          placeholder="Nom, prénom, e-mail..."
          className="border px-3 py-2 rounded w-full"
        />

          <button
            type="button"
            aria-label="Réinitialiser le champ de recherche"
            onClick={onClear}
            className="flex items-center justify-center text-[var(--color-dark-purple)] hover:text-[var(--color-block-purple-hover)] transition"
          >
            <AiOutlineCloseCircle size={22} aria-hidden="true" />
          </button>
          </div>

        <Button
          title="Rechercher"
          onClick={onSearch}
          marginTop="mt-0"
          paddingY="py-1"
          className="text-sm w-32 pr-2 self-center"
        />
      </div>
    </section>
  );
}
