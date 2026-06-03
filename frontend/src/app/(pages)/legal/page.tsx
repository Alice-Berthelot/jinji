import BackArrow from "@/components/ui/BackArrow";
import MainTitle from "@/components/ui/MainTitle";
import Subtitle from "@/components/ui/Subtitle";
import jinjiLogo from "../../../../public/logos/jinji_logo.svg";
import Image from "next/image";

export default function LegalMentionsPage() {
  return (
    <>
      <BackArrow />

      <div className="flex flex-col lg:flex-row lg:justify-between">
        <MainTitle title="Mentions légales" />
      </div>

      <div className="flex flex-col mx-auto w-[95%] lg:w-[92%] mt-6">
        <section className="bg-[var(--color-block-white)] px-6 py-6 shadow-sm rounded-sm space-y-10 mb-8">
          <Image
            src={jinjiLogo}
            alt="logo de l'application Jinji"
            width={60}
            className="mx-auto text-center"
          />

          <div className="space-y-2">
            <Subtitle subtitle="Éditeur du site" />
            <div className="text-sm space-y-1">
              <p>
                <strong>Projet :</strong> Jinji
              </p>
              <p>
                <strong>Nature :</strong> Application SIRH
              </p>
              <p>
                <strong>Statut :</strong> Projet pédagogique CDA – non déployé
                en production
              </p>
              <p>
                <strong>Développeur :</strong> Alice Berthelot
              </p>
              <p>
                <strong>E-mail :</strong>{" "}
                <a
                  href="mailto:aliceberthelot.pro@gmail.com"
                  className="text-[var(--color-dark-purple)] underline"
                >
                  aliceberthelot.pro@gmail.com
                </a>
              </p>
            </div>
          </div>

          <div className="space-y-2">
            <Subtitle subtitle="Objet du projet" />
            <p className="text-sm">
              Jinji est une application fictive de gestion des ressources
              humaines (SIRH). Elle permet de simuler la gestion des absences,
              ainsi qu’un espace intranet/extranet pour les collaborateurs et
              managers.
            </p>
          </div>

          <div className="space-y-2">
            <Subtitle subtitle="Hébergement" />
            <p className="text-sm">
              Le projet est exécuté dans un cadre de développement et de
              démonstration. Il n’est pas déployé dans un environnement de
              production.
            </p>
          </div>

          <div className="space-y-2">
            <Subtitle subtitle="Données personnelles" />
            <p className="text-sm">
              Ce projet étant fictif, aucune donnée réelle n’est collectée ni
              exploitée. Les données utilisées sont strictement simulées dans un
              cadre pédagogique.
            </p>
          </div>

          <div className="space-y-2">
            <Subtitle subtitle="Propriété intellectuelle" />
            <p className="text-sm">
              Le code source, l’interface et le design de Jinji sont protégés
              dans le cadre du projet de formation. Toute réutilisation hors
              cadre pédagogique nécessite l’accord de l’auteur.
            </p>
          </div>

          <div className="space-y-2">
            <Subtitle subtitle="Responsabilité" />
            <p className="text-sm">
              Ce projet étant réalisé dans un cadre pédagogique, il peut
              contenir des erreurs ou limitations et n’a aucune valeur
              contractuelle ou commerciale.
            </p>
          </div>

          <div className="space-y-2 mb-4">
            <Subtitle subtitle="Droit applicable" />
            <p className="text-sm">
              Ce projet est soumis au droit français et réalisé dans le cadre
              d’une formation Concepteur Développeur d’Applications (CDA).
            </p>
          </div>
        </section>
      </div>
    </>
  );
}
