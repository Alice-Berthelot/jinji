"use client";

import { getMyLeaveRequestsSummary } from "@/app/api/leave-requests/me/route";
import LeaveRequestSmallTable from "@/components/tables/LeaveRequestSmallTable";
import MainTitle from "@/components/ui/MainTitle";
import Subtitle from "@/components/ui/Subtitle";
import { MyLeaveRequestsSummary } from "@/types/leave/leaveRequest";
import { useEffect, useState } from "react";

export default function LeavePage() {
  const [leaveRequests, setLeaveRequests] = useState<
    MyLeaveRequestsSummary[] | []
  >([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function load() {
      try {
        setLoading(true);
        const data = await getMyLeaveRequestsSummary();
        setLeaveRequests(data);
        setLoading(false);
      } catch (err) {
        console.error(err);
      }
    }
    load();
  }, []);

  return (
    <>
      <MainTitle title="Mes congés et absences" />
      <div className="flex flex-col lg:flex-row justify-center items-center lg:items-start gap-4 lg:gap-8 lg:h-96">
        <section className="bg-[var(--color-block-white)] px-2 py-6 shadow-sm rounded-sm w-[95%] lg:w-[45%] lg:h-full">
          <Subtitle subtitle="Mon solde de congés payés" />
          {/* <PaidLeaveAccount /> */}
        </section>
        <section className="bg-[var(--color-block-white)] px-2 py-6 shadow-sm rounded-sm w-[95%] lg:w-[45%] lg:h-full">
          <Subtitle subtitle="Mes demandes d'absence" />
          <LeaveRequestSmallTable leaveRequests={leaveRequests} loading={loading}/>
        </section>
      </div>
      <section className="mx-auto lg:ml-10 mt-4 lg:mt-8 mb-4 bg-[var(--color-block-white)] px-2 py-6 shadow-sm rounded-sm w-[95%] lg:w-[93%] lg:mx-4">
        <Subtitle subtitle="Mon planning" />
        {/* <EmployeePlanning /> */}
      </section>
    </>
  );
}
