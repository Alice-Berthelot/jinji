"use client";

import { useRouter, useSearchParams } from "next/navigation";
import { useState } from "react";
import Button from "./Button";
import Subtitle from "./Subtitle";
import Input from "./Input";

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

  return (
    <section>
      <Subtitle subtitle="Rechercher un collaborateur" />
      <div className="flex gap-4 items-center w-3/5">
        <Input
          value={value}
          onChange={(e) => setValue(e.target.value)}
          placeholder="Nom, prénom, email..."
          className="border px-3 py-2 rounded w-full"
        />

        <Button title="Rechercher" onClick={onSearch} marginTop="mt-0" paddingY="py-1" className="text-sm w-32" />
      </div>
    </section>
  );
}
