import type { Metadata } from "next";
import { Inclusive_Sans, Nunito_Sans } from "next/font/google";
import "./globals.css";
import AuthToastHandler from "@/components/ui/AuthAlertHandler";
import { ToastContainer } from "react-toastify";
import Header from "@/components/layout/Header";
import Sidebar from "@/components/layout/Sidebar";
import { getUserRoles } from "@/lib/auth";

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

export default async function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const roles = await getUserRoles();
  return (
    <html lang="en">
      <body
        className={`${inclusiveSans.variable} ${nunitoSans.variable} antialiased`}
      >
        <Header />
        <div className="lg:flex">
          <Sidebar roles={roles} />
          <main className="flex-1">
            <AuthToastHandler />
            <ToastContainer />
            {children}
          </main>
        </div>
      </body>
    </html>
  );
}
