import type { Metadata } from "next";
import { Inclusive_Sans, Nunito_Sans } from "next/font/google";
import "./globals.css";
import AuthToastHandler from "@/components/ui/AuthAlertHandler";
import { ToastContainer } from "react-toastify";

const inclusiveSans = Inclusive_Sans({
  variable: "--font-inclusive-sans",
  subsets: ["latin"],
});

const nunitoSans = Nunito_Sans({
  variable: "--font-nunito-sans",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "jinji",
  description:
    "multi-company HRIS application specializing in paid leave management",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body
        className={`${inclusiveSans.variable} ${nunitoSans.variable} antialiased`}
      >
        <main>
          <AuthToastHandler />
          <ToastContainer />
          {children}
        </main>
      </body>
    </html>
  );
}
