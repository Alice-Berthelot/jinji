import MainTitle from "@/components/ui/MainTitle";
import jinjiLogo from "../../../../public/logos/jinji_logo.svg";
import Image from "next/image";
import LoginForm from "@/components/forms/LoginForm";

export default function Login() {
  return (
    <section className="flex flex-col items-center">
      <Image
        src={jinjiLogo}
        alt="logo de l'application Jinji"
        width={90}
        className="mt-15 mx-auto text-center"
      />
      <p className="tracking-wide text-xl text-center font-medium mt-2">jinji</p>
      <div className="w-96">
      <MainTitle title={"Connexion"} marginTop="mt-15 md:mt-20" marginBottom="mb-0" paddingLeft="pl-4" />
      <LoginForm />
      </div>
    </section>
  );
}
