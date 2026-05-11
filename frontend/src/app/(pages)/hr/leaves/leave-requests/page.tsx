"use client";

import { LeaveRequestsSummary } from "@/types/leave/leaveRequest";
import { useEffect, useState } from "react";
import { getLeaveRequestsSummary } from "@/app/api/leave-requests/me/route";
import LeaveRequestsList from "@/components/LeaveRequestsList";
import BackArrow from "@/components/ui/BackArrow";
import MainTitle from "@/components/ui/MainTitle";

export default function HrLeaveRequestsPage() {
  const [leaveRequests, setLeaveRequests] = useState<LeaveRequestsSummary[]>(
    []
  );
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function load() {
      try {
        setLoading(true);
        const data = await getLeaveRequestsSummary();
        console.log(data);
        setLeaveRequests(data);
        setLoading(false);
      } catch (err) {
        console.error(err);
      }
    }
    load();
  }, []);

  if (loading) {
    return <p>Chargement...</p>;
  }

  return (
    <>
      <BackArrow />
      <MainTitle title="Demandes d'absence des équipes" />
      <section className="m-auto lg:my-0 lg:mx-8 bg-[var(--color-block-white)] px-6 py-4 shadow-sm rounded-sm w-[95%] lg:min-h-screen">
        <div className="flex flex-col justify-center items-center gap-4 lg:gap-8 mb-6">
          <LeaveRequestsList
            leaveRequests={leaveRequests}
            role="HR"
            detailBasePath="/hr/leaves/leave-requests"
          />
        </div>
      </section>
    </>
  );
}
