'use client';

import MainTitle from "@/components/ui/MainTitle";
import LinkPurple from "@/components/ui/LinkCustom";

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
        <button
          onClick={() => reset()}
          className="px-4 py-2 rounded bg-purple-600 text-white hover:bg-purple-700 transition"
        >
          Réessayer
        </button>

        <LinkPurple href="/" title="Accueil" />
      </div>
    </div>
  );
}