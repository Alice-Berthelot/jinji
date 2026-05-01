import MainTitle from "@/components/ui/MainTitle";
import jinjiLogo from "../../../../public/logos/jinji_logo.svg";
import Image from "next/image";
import LoginForm from "@/components/forms/LoginForm";

export default function Login() {
  return (
    <>
      <Image
        src={jinjiLogo}
        alt="logo de l'application Jinji"
        width={90}
        className="mt-15 mx-auto text-center"
      />
      <MainTitle title={"Connexion"} marginTop="mt-2" marginBottom="mb-6" textCenter={true} paddingLeft="pl-0" />
      <LoginForm />
    </>
  );
}
