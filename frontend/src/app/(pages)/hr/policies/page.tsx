import LeaveValidationUpdate from "@/components/LeaveValidationUpdate";
import MainTitle from "@/components/ui/MainTitle";
import Subtitle from "@/components/ui/Subtitle";
import { getLeaveValidation } from "@/services/hrPolicy.service";

export default async function HrPoliciesSetting() {
  const initialValue = await getLeaveValidation();
  return (
    <>
      <MainTitle title="Paramétrage des règles RH" marginTop="mt-24" />
      <section className="m-auto lg:my-0 lg:mx-8 bg-[var(--color-block-white)] px-6 py-4 shadow-sm rounded-sm w-[95%] min-h-screen">
        <Subtitle subtitle="Paramétrage des règles liées à la gestion des Ressources humaines" />
        <div className="flex flex-wrap justify-center gap-8 mt-18">
          <LeaveValidationUpdate initialValue={initialValue}/>
        </div>
      </section>
    </>
  );
}
