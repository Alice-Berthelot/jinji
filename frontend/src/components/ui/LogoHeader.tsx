import jinjiLogo from "../../../public/logos/jinji_logo.svg";
import Image from "next/image";
import Link from "next/link";

export default function LogoHeader() {
  return (
    <Link href="/" aria-label="Retour à l'accueil">
    <Image src={jinjiLogo} alt="logo de l'application Jinji" width={30} />
    </Link>
  );
}