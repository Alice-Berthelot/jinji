import MainTitle from "@/components/ui/MainTitle";
import Link from "next/link";
import jinjiLogo from "../../public/logos/jinji_logo.svg";
import Image from "next/image";
import LinkPurple from "@/components/ui/LinkPurple";

export default function NotFound() {
  return (
    <div className="flex flex-col items-center justify-center">
      <MainTitle title="404" paddingLeft="0" />

      <p className="text-xl mb-10">Oups… la page que vous recherchez n'existe pas.</p>

      <LinkPurple href="/" title="Accueil" />
    </div>
  );
}
