import MainTitle from "@/components/ui/MainTitle";
import LinkPurple from "@/components/ui/LinkCustom";

export default function NotFound() {
  return (
    <div className="flex flex-col items-center justify-center">
      <MainTitle title="404" paddingLeft="0" />

      <p className="text-xl mb-10">Oups… la page que vous recherchez n'existe pas.</p>

      <LinkPurple href="/" title="Accueil" />
    </div>
  );
}
