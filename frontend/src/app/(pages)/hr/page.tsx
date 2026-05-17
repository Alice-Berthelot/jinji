import LinkPurple from "@/components/ui/LinkCustom";
import { hasRole } from "@/lib/auth";

export default async function HrHome() {
  const isHR = await hasRole("HR");

  return (
    <>
      <p>HR test</p>
      <LinkPurple href="/hr/employees/new-employee" title="Ajouter un collaborateur" />
    </>
  );
}
