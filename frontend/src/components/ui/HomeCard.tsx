import Link from "next/link";
import { IconType } from "react-icons";

type NavLink = {
  name: string;
  link: string;
  icon?: IconType;
};

interface HomeCardProps {
  link: NavLink;
}

export default function HomeCard({ link }: HomeCardProps) {
  const Icon = link.icon;

  return (
    <article>
      <Link
        href={link.link}
        className="
          flex lg:flex-col items-center justify-between lg:justify-center gap-4 lg:gap-6
          rounded-2xl
          bg-[var(--color-block-purple)]
          p-6
          transition-colors
          hover:bg-[var(--color-block-purple-hover)]
          focus-visible:outline-2
          focus-visible:outline-offset-2
          focus-visible:outline-[var(--color-dark-purple)]
          lg:text-center
          w-80 lg:h-40
        "
      >
        <h3 className="text-xl font-semibold">{link.name}</h3>

        {Icon && (
          <Icon
            aria-hidden="true"
            className="shrink-0 text-3xl text-[var(--color-dark-purple)]"
          />
        )}
      </Link>
    </article>
  );
}