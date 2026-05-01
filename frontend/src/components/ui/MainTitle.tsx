interface MainTitleProps {
  title: string;
  marginTop?: string;
  marginBottom?: string;
  paddingLeft?: string;
  textCenter?: boolean;
}

export default function MainTitle({
  title,
  marginTop,
  marginBottom,
  paddingLeft,
  textCenter,
}: MainTitleProps) {
  return (
    <h1
      className={`${marginTop ? marginTop : "mt-8 md:mt-22"} ${
        marginBottom ? marginBottom : "mb-10"
      } ${textCenter ? "text-center" : "text-left"} text-3xl ${paddingLeft ? paddingLeft : "pl-6"}`}
    >
      {title}
    </h1>
  );
}
