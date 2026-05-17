"use client";

import LinkPurple from "@/components/ui/LinkCustom";
import { hasRole } from "@/lib/auth";
import { useEffect, useState } from "react";

export default function HrHome() {
  const [isHR, setIsHR] = useState<boolean>(false);

  useEffect(() => {
    async function getRole() {
      try {
        const isHR = await hasRole("HR");
        setIsHR(isHR);
      } catch (err) {
        console.error(err);
      }
    }
    getRole();
  }, []);

  return (
    <>
      <p>HR test</p>
      <LinkPurple href="/hr/new-employee" title="Ajouter un collaborateur" />
    </>
  );
}
