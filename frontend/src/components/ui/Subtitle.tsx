interface MainTitleProps {
  subtitle: string;
  marginTop?: string;
  marginBottom?: string;
  paddingLeft?: string;
  className?: string;
}

export default function Subtitle({
  subtitle,
  marginTop,
  marginBottom,
  paddingLeft,
  className,
}: MainTitleProps) {
  return (
    <h2
      className={`${marginTop ? marginTop : "mt-0"} ${
        marginBottom ? marginBottom : "mb-6"
      } text-md ${paddingLeft ? paddingLeft : "pl-2 lg:pl-6"} font-bold ${className}`}
    >
      {subtitle}
    </h2>
  );
}
