'use client';

import MainTitle from "@/components/ui/MainTitle";
import LinkPurple from "@/components/ui/LinkCustom";
import Button from "@/components/ui/Button";

export default function Error({
  reset,
}: {
  reset: () => void;
}) {
  return (
    <div className="flex flex-col items-center justify-center">
      <MainTitle title="Erreur" paddingLeft="0" />

      <p className="text-xl mb-10">
        Oups… une erreur inattendue est survenue.
      </p>

      <div className="flex gap-4">
        <Button onClick={() => reset()}  title="Réessayer" />
      </div>
    </div>
  );
}